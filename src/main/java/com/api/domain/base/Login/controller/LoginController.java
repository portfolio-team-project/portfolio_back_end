package com.api.domain.base.Login.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.base.Login.dto.LoginRequest;
import com.api.domain.base.Login.dto.LoginResponse;
import com.api.domain.base.Member.entity.MemberEntity;
import com.api.domain.base.Member.service.MemberService;
import com.api.global.redis.RedisService;
import com.api.global.security.jwt.JwtProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {
	
	private final MemberService memberService;
	private final RedisService redisService;
	private final JwtProvider jwtProvider;
	
	@PostMapping("/login")
	public LoginResponse login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
		
		MemberEntity member = memberService.login(request.getUserId(), request.getPassword());
		
		String isRole = memberService.getRole(member);

	    String accessToken = jwtProvider.createToken(member.getUserId(),isRole);
	    String refreshToken = jwtProvider.createRefreshToken(member.getUserId());

	    // Redis 저장
	    redisService.saveRefreshToken(member.getUserId(), refreshToken);

	    // HttpOnly Cookie 생성
	    Cookie cookie = new Cookie("refreshToken", refreshToken);
	    cookie.setHttpOnly(true);   // JS 접근 불가
	    cookie.setSecure(true);     // HTTPS에서만 전송 (운영 필수)
	    cookie.setPath("/refresh"); // refresh API에서만 사용
	    cookie.setMaxAge(7 * 24 * 60 * 60); // 7일

	    response.addCookie(cookie);

	    return new LoginResponse(accessToken);
	}
}
