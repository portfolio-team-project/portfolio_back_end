package com.api.domain.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.auth.dto.RefreshRequest;
import com.api.global.redis.RedisService;
import com.api.global.security.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	
	private final JwtProvider jwtProvider;
	private final RedisService redisService;
	
	@PostMapping("/refresh")
	public void refresh(@RequestBody RefreshRequest request) {
		
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
	    
	    // Redis 값 비교
	    if (
	        savedRefreshToken == null
	        || !savedRefreshToken.equals(refreshToken)
	    ) {
	        throw new RuntimeException("refresh token mismatch");
	    }
	    
	    // 새 access token 발급
	    String newAccessToken =
	            jwtProvider.createToken(username);

	    // 새 refresh token 발급 (선택)
	    String newRefreshToken =
	            jwtProvider.createRefreshToken(username);

	    // Redis 갱신
	    redisService.saveRefreshToken(
	            username,
	            newRefreshToken
	    );
	    
		/*
		 * return new LoginResponse( newAccessToken, newRefreshToken );
		 */
	} 
}
