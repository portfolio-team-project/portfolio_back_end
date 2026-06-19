package com.api.domain.auth.controller;

import java.util.Arrays;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.auth.dto.AuthResponse;
import com.api.domain.auth.dto.SocialLoginRequest;
import com.api.domain.auth.service.KaKaoService;
import com.api.domain.base.Login.dto.LoginResponse;
import com.api.domain.base.Login.service.LoginService;
import com.api.domain.base.Member.entity.MemberEntity;
import com.api.domain.base.Member.service.MemberService;
import com.api.global.common.ApiResponse;
import com.api.global.constants.MessageConstants;
import com.api.global.exception.BusinessException;
import com.api.global.redis.RedisService;
import com.api.global.security.jwt.JwtProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Log4j2
public class AuthController {
	
	private final JwtProvider jwtProvider;
	private final RedisService redisService;
	private final MemberService memberService;
	private final KaKaoService kakaoService;
	private final LoginService loginService;
	
	@PostMapping("/refresh")
	public ResponseEntity<ApiResponse<LoginResponse>> refresh(HttpServletRequest request, HttpServletResponse response) {
		
		Cookie[] cookies = request.getCookies();
		if (cookies == null) throw new BusinessException(MessageConstants.COOKIE_NOT_FOUND);
		
		// 쿠키에서 refreshToken 꺼내기
	    String refreshToken = Arrays.stream(request.getCookies())
	            .filter(c -> "refreshToken".equals(c.getName()))
	            .findFirst()
	            .map(Cookie::getValue)
	            .orElseThrow(() -> new BusinessException(MessageConstants.REFRESH_TOKEN_NOT_FOUND));
		
		// refresh token 유효성 검사
	    if (!jwtProvider.validateToken(refreshToken)) {
	        throw new BusinessException(MessageConstants.REFRESH_TOKEN_INVALID);
	    }
	    
	    // uuid 추출
	    String uuid =
	            jwtProvider.getUuid(refreshToken);

	    // Redis 저장값 조회
	    String savedRefreshToken =
	            redisService.getRefreshToken(uuid);
	    
	    // Redis 값 비교
	    if (
	        savedRefreshToken == null
	        || !savedRefreshToken.equals(refreshToken)
	    ) {
	        throw new BusinessException(MessageConstants.REFRESH_TOKEN_MISMATCH);
	    }
	    
	    // 권한 조회 ( 추후 db 붙으면 추가 필요 )
	    MemberEntity member = memberService.findByUuid(uuid);
	    String isRole = memberService.getRole(member);
	    String isSocial = (member.getKakaoId() != null && !member.getKakaoId().isEmpty()) ? "N":"Y";
	    
	    // 새 access token 발급
	    String newAccessToken =
	            jwtProvider.createToken(uuid,isRole);

	    // 새 refresh token 발급 (선택)
	    String newRefreshToken =
	            jwtProvider.createRefreshToken(uuid);

	    // Redis 갱신
	    redisService.saveRefreshToken(
	            uuid,
	            newRefreshToken
	    );
	    
	    Cookie cookie = new Cookie("refreshToken", newRefreshToken);
	    cookie.setHttpOnly(true);
	    cookie.setSecure(true);
	    cookie.setPath("/api/auth/refresh");
	    cookie.setMaxAge(7 * 24 * 60 * 60);
	    response.addCookie(cookie);
	    
		
		return ResponseEntity.ok(ApiResponse.ok(new LoginResponse(newAccessToken,member.getUserId(),member.getUserName(),isRole,isSocial)));
	}
	
	@PostMapping("/social/{provider}")
	public ResponseEntity<ApiResponse<LoginResponse>> socialLogin(@PathVariable String provider, @RequestBody SocialLoginRequest request, HttpServletRequest httpRequest, HttpServletResponse response) {
	    
	    MemberEntity member;
	    if (provider.equals("kakao")) {
	        try {
	            member = kakaoService.login(request.getCode());
	        } catch (BusinessException e) {
	            loginService.saveLog(null, httpRequest, "N", e.getMessage());
	            throw e;  // 다시 던져서 글로벌 핸들러로
	        } catch (Exception e) {
	            log.error("카카오 로그인 오류: {}", e.getMessage(), e);
	            loginService.saveLog(null, httpRequest, "N", e.getMessage());
	            throw new BusinessException("카카오 로그인 처리 중 오류가 발생했습니다.");
	        }
	    } else {
	        throw new BusinessException("지원하지 않는 소셜 로그인입니다.");
	    }
	    
	    loginService.saveLog(member, httpRequest, "Y", null);
        
        String isRole = memberService.getRole(member);
        String isSocial = (member.getKakaoId() != null && !member.getKakaoId().isEmpty()) ? "N":"Y";

        String accessToken = jwtProvider.createToken(member.getUuid(),isRole);
        String refreshToken = jwtProvider.createRefreshToken(member.getUuid());

        // Redis 저장
        redisService.saveRefreshToken(member.getUuid(), refreshToken);

        // HttpOnly Cookie 생성
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);   // JS 접근 불가
        cookie.setSecure(true);     // HTTPS에서만 전송 (운영 필수)
        cookie.setPath("/api/auth/refresh"); // refresh API에서만 사용
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7일

        response.addCookie(cookie);
	    
        return ResponseEntity.ok(ApiResponse.ok(new LoginResponse(accessToken, member.getUserId(), member.getUserName(),isRole,isSocial)));
    }
}
