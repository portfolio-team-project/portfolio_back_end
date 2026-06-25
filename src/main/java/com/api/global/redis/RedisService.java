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
	
	/*
	 * 인증번호 저장
	 * */
	public void saveCertNum(String uuid, String certNum) {
	    redisTemplate.opsForValue().set("CERT:"+uuid, certNum, 3, TimeUnit.MINUTES);
	}
	
	/*
	 * 인증번호 불러오기
	 * */
	public String getCertNum(String uuid) {
	    return redisTemplate.opsForValue().get("CERT:"+uuid);
	}
	/*
	 * 인증 완료 여부
	 * */
	public void saveVerified(String uuid) {
	    redisTemplate.opsForValue().set("VERIFIED:" + uuid, "true", 5, TimeUnit.MINUTES);
	}
	
	/*
	 * 인증 완료 확인
	 * */
	public boolean getVerified(String uuid) {
	    return "true".equals(redisTemplate.opsForValue().get("VERIFIED:"+uuid));
	}
	
	/*
	 * 확인 완료된 인증 여부 삭제
	 * */
	public void deleteVerified(String uuid) {
	    redisTemplate.delete("VERIFIED:"+uuid);
	}

	/*
	 * 회원가입 이메일 인증번호 저장
	 * */
	public void saveSignupCertNum(String email, String certNum) {
	    redisTemplate.opsForValue().set("SIGNUP_CERT:" + email, certNum, 3, TimeUnit.MINUTES);
	}

	/*
	 * 회원가입 이메일 인증번호 조회
	 * */
	public String getSignupCertNum(String email) {
	    return redisTemplate.opsForValue().get("SIGNUP_CERT:" + email);
	}

	/*
	 * 회원가입 이메일 인증 완료 저장
	 * */
	public void saveSignupVerified(String email) {
	    redisTemplate.opsForValue().set("SIGNUP_VERIFIED:" + email, "true", 5, TimeUnit.MINUTES);
	}

	/*
	 * 회원가입 이메일 인증 완료 확인
	 * */
	public boolean getSignupVerified(String email) {
	    return "true".equals(redisTemplate.opsForValue().get("SIGNUP_VERIFIED:" + email));
	}

	/*
	 * 회원가입 이메일 인증 완료 삭제
	 * */
	public void deleteSignupVerified(String email) {
	    redisTemplate.delete("SIGNUP_VERIFIED:" + email);
	}
	
	//비밀번호 찾기 세션 횟수 제한
	public void saveSessionToken(String sessionToken, String uuid) {
	    redisTemplate.opsForValue().set("SESSION:" + sessionToken, uuid, 3, TimeUnit.MINUTES);
	}

	public String getSessionToken(String sessionToken) {
	    return redisTemplate.opsForValue().get("SESSION:" + sessionToken);
	}

	public void deleteSessionToken(String sessionToken) {
	    redisTemplate.delete("SESSION:" + sessionToken);
	}
	
	public Long incrementAttempt(String sessionToken) {
	    Long count = redisTemplate.opsForValue().increment("ATTEMPT_SESSION:" + sessionToken);
	    if (count == 1) {
	        redisTemplate.expire("ATTEMPT:" + sessionToken, 3, TimeUnit.MINUTES);
	    }
	    return count;
	}

}
