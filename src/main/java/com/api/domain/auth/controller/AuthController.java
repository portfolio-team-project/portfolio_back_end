package com.api.domain.auth.controller;

import java.util.Arrays;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.auth.dto.AuthResponse;
import com.api.domain.base.Member.entity.MemberEntity;
import com.api.domain.base.Member.service.MemberService;
import com.api.global.exception.BusinessException;
import com.api.global.redis.RedisService;
import com.api.global.security.jwt.JwtProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	
	private final JwtProvider jwtProvider;
	private final RedisService redisService;
	private final MemberService memberService;
	
	@PostMapping("/refresh")
	public AuthResponse refresh(HttpServletRequest request, HttpServletResponse response) {
		
		// 쿠키에서 refreshToken 꺼내기
	    String refreshToken = Arrays.stream(request.getCookies())
	            .filter(c -> "refreshToken".equals(c.getName()))
	            .findFirst()
	            .map(Cookie::getValue)
	            .orElseThrow(() -> new BusinessException("refresh token not found"));
		
		// refresh token 유효성 검사
	    if (!jwtProvider.validateToken(refreshToken)) {
	        throw new BusinessException("invalid refresh token");
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
	        throw new BusinessException("refresh token mismatch");
	    }
	    
	    // 권한 조회 ( 추후 db 붙으면 추가 필요 )
	    MemberEntity member = memberService.findByUuid(uuid);
	    String authToken = memberService.getRole(member);
	    
	    // 새 access token 발급
	    String newAccessToken =
	            jwtProvider.createToken(uuid,authToken);

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
	    cookie.setPath("/refresh");
	    cookie.setMaxAge(7 * 24 * 60 * 60);
	    response.addCookie(cookie);
	    
		
		return new AuthResponse( newAccessToken );
	}
	
}
