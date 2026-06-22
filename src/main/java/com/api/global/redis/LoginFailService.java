package com.api.global.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginFailService {
	
	private final StringRedisTemplate redisTemplate;
	
	public void increaseLoginFailCount(String username) {

	    String key = "LOGIN_FAIL:" + username;

	    Long count = redisTemplate.opsForValue().increment(key);

	    // 첫 실패일 때만 만료시간 설정
	    if (count != null && count == 1) {
	        redisTemplate.expire(key, 10, TimeUnit.MINUTES);
	        }
	}
	
	public int getLoginFailCount(String username) {

	    String value = redisTemplate.opsForValue()
	                                .get("LOGIN_FAIL:" + username);

	    return value == null ? 0 : Integer.parseInt(value);
	}
	
	public void clearLoginFailCount(String username) {

	    redisTemplate.delete("LOGIN_FAIL:" + username);
	}
	
	public void increaseChgPwdFailCount(String username) {

	    String key = "CHG_PWD_FAIL:" + username;

	    Long count = redisTemplate.opsForValue().increment(key);

	    // 첫 실패일 때만 만료시간 설정
	    if (count != null && count == 1) {
	        redisTemplate.expire(key, 30, TimeUnit.MINUTES);
	        }
	}
	
	public int getChgPwdFailCount(String username) {

	    String value = redisTemplate.opsForValue()
	                                .get("CHG_PWD_FAIL:" + username);

	    return value == null ? 0 : Integer.parseInt(value);
	}
	
	public void clearChgPwdFailCount(String username) {

	    redisTemplate.delete("CHG_PWD_FAIL:" + username);
	}
}
