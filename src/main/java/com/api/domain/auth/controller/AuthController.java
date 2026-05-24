package com.api.domain.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.auth.dto.AuthResponse;
import com.api.domain.auth.dto.RefreshRequest;
import com.api.domain.base.Member.entity.MemberEntity;
import com.api.domain.base.Member.service.MemberService;
import com.api.global.redis.RedisService;
import com.api.global.security.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	
	private final JwtProvider jwtProvider;
	private final RedisService redisService;
	private final MemberService memberService;
	
	@PostMapping("/refresh")
	public AuthResponse refresh(@RequestBody RefreshRequest request) {
		
		String refreshToken = request.getRefreshToken();
		
		// refresh token 유효성 검사
	    if (!jwtProvider.validateToken(refreshToken)) {
	        throw new RuntimeException("invalid refresh token");
	    }
	    
	    // username 추출
	    String username =
	            jwtProvider.getUsername(refreshToken);

	    // Redis 저장값 조회
	    String savedRefreshToken =
	            redisService.getRefreshToken(username);
	    
	    // 권한 조회 ( 추후 db 붙으면 추가 필요 )
	    MemberEntity member = memberService.findByUserId(username);
	    String authToken = memberService.getRole(member);
	    
	    // Redis 값 비교
	    if (
	        savedRefreshToken == null
	        || !savedRefreshToken.equals(refreshToken)
	    ) {
	        throw new RuntimeException("refresh token mismatch");
	    }
	    
	    // 새 access token 발급
	    String newAccessToken =
	            jwtProvider.createToken(username,authToken);

	    // 새 refresh token 발급 (선택)
	    String newRefreshToken =
	            jwtProvider.createRefreshToken(username);

	    // Redis 갱신
	    redisService.saveRefreshToken(
	            username,
	            newRefreshToken
	    );
	    
		
		return new AuthResponse( newAccessToken, newRefreshToken );
	}
	
}
