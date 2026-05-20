package com.api.global.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {
	
	private final RedisTemplate<String, String> redisTemplate;
	
	/*
	 * refresh 토큰 저장
	 * */
	public void saveRefreshToken(String username, String refreshToken) {
		/*
		 * 7일간 보관 후 자동 삭제
		 * */
		redisTemplate.opsForValue().set("RT:"+username, refreshToken, 7, TimeUnit.DAYS);
	}
	
	/*
	 * refresh 토큰 가져오기
	 * */
	public String getRefreshToken(String username) {
		
		return (String) redisTemplate.opsForValue().get("RT:"+username);
	}
	
	/*
	 * refresh 토큰 삭제
	 * */
	public void deleteRefreshToken(String username) {

	    redisTemplate.delete("RT:"+username);
	}
	
	
}
