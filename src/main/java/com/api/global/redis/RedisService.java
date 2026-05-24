package com.api.global.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {
	
	private final StringRedisTemplate redisTemplate;
	
	/*
	 * refresh 토큰 저장
	 * */
	public void saveRefreshToken(String uuid, String refreshToken) {
		/*
		 * 7일간 보관 후 자동 삭제
		 * */
		redisTemplate.opsForValue().set("RT:"+uuid, refreshToken, 7, TimeUnit.DAYS);
	}
	
	/*
	 * refresh 토큰 가져오기
	 * */
	public String getRefreshToken(String uuid) {
		
		return (String) redisTemplate.opsForValue().get("RT:"+uuid);
	}
	
	/*
	 * refresh 토큰 삭제
	 * */
	public void deleteRefreshToken(String uuid) {

	    redisTemplate.delete("RT:"+uuid);
	}
	
	
}
