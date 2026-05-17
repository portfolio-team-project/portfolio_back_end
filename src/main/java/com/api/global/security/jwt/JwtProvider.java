package com.api.global.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * 
 * @author lee
 * @version 1.0
 */
@Component
public class JwtProvider {
	@Value("${jwt.secret}")
    private String secret;
	
	/*
	 * String key값을 Secret Key 값으로 변환
	 * */
	private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }
	
    //토큰 생성 1시간
    public String createToken(String username) {
    	return Jwts.builder()
                   .subject(username)
                   .issuedAt(new Date())
                   .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                   .signWith(getSigningKey())
                   .compact();
    }
    
    //refresh 토큰 생성 Redis용 7일
    public String createRefreshToken(String username) {
    	return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .signWith(getSigningKey())
                .compact();
    }
    
    /*
     * 유효성 검사
     * token과 실제 Secret Key 비교
     * 맞으면 True, 실패 시 False 반환
     * */
    public boolean validateToken(String token) {

        try {

            Jwts.parser().verifyWith(getSigningKey())
                         .build()
                         .parseSignedClaims(token);

            return true;

        } catch (Exception e) {
            return false;
        }
    }
    
    /*
     * payload 안에 있는 user 값 반환
     * */
    public String getUsername(String token) {

        return Jwts.parser()
                   .verifyWith(getSigningKey())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload()
                   .getSubject();
    }
}
