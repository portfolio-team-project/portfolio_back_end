package com.api.domain.base.Login.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.base.Login.dto.LoginRequest;
import com.api.domain.base.Login.dto.LoginResponse;
import com.api.domain.base.Login.service.LoginService;
import com.api.domain.base.Member.entity.MemberEntity;
import com.api.domain.base.Member.service.MemberService;
import com.api.global.common.ApiResponse;
import com.api.global.constants.MessageConstants;
import com.api.global.exception.BusinessException;
import com.api.global.redis.LoginFailService;
import com.api.global.redis.RedisService;
import com.api.global.security.jwt.JwtProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {
	
	private final MemberService memberService;
	private final RedisService redisService;
	private final JwtProvider jwtProvider;
	private final LoginFailService loginFailService;
	private final LoginService loginService;
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse response) {
		
		// 1. 잠금 체크 (5회 이상 실패 시 차단)
	    int failCount = loginFailService.getLoginFailCount(request.getUserId());
	    if (failCount >= 5) {
	        
	        loginService.saveLog(null, httpRequest, "N", MessageConstants.LOGIN_LOCKED);
	        throw new BusinessException(MessageConstants.LOGIN_LOCKED);
	    }
	    
	    MemberEntity member;
	    try {
	    	member = memberService.login(request.getUserId(), request.getPassword());
	    
    	    //성공 시 카운트 초기화
    	    loginFailService.clearLoginFailCount(request.getUserId());
    	    
    	    loginService.saveLog(member, httpRequest, "Y", null);
    	    
    	    //TO-DO 3개월 주기 로그인 체크 필요
    	    memberService.checkPasswordExpired(member);
    		
    		String isRole = memberService.getRole(member);
    
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
    
    	    return ResponseEntity.ok(ApiResponse.ok(new LoginResponse(accessToken,member.getUserId(),member.getUserName())));
	    
	    } catch (Exception e) {
	        if (MessageConstants.PWD_EXPIRED.equals(e.getMessage())) {
	            throw (BusinessException)e;
	        }
            // 실패 시 카운트 증가
            loginFailService.increaseLoginFailCount(request.getUserId());
            
            loginService.saveLog(null, httpRequest, "N", e.toString());
            
            throw new BusinessException(MessageConstants.LOGIN_FAILED);
        }
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletResponse response) {
		String uuid = (String) SecurityContextHolder.getContext()
				                                    .getAuthentication()
                                                    .getPrincipal();

		redisService.deleteRefreshToken(uuid);
		
		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/api/auth/refresh");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		
		return ResponseEntity.ok().build();
	}
}
