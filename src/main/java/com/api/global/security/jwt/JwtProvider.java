package com.api.global.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
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
	@Value("${jwt.expiration}")
	private Long expiration;
	@Value("${jwt.refresh-expiration}")
	private Long refreshExpiration;
	
	/*
	 * String key값을 Secret Key 값으로 변환
	 * */
	private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }
	
    //토큰 생성 1시간
    public String createToken(String uuid, String role) {
    	return Jwts.builder()
                   .subject(uuid)
                   .claim("type", "access")
                   .claim("role",role)
                   .issuedAt(new Date())
                   .expiration(new Date(System.currentTimeMillis() + expiration))
                   .signWith(getSigningKey())
                   .compact();
    }
    
    //refresh 토큰 생성 Redis용 7일
    public String createRefreshToken(String uuid) {
    	return Jwts.builder()
                .subject(uuid)
                .claim("type","refresh")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
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

        } catch (ExpiredJwtException e) {
            return false;
        } catch (SecurityException e) {
            return false;
        } catch (MalformedJwtException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /*
     * role 조회
     * */
    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }
    
    public String getTokenType(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("type", String.class);
    }
    
    /*
     * payload 안에 있는 user 값 반환
     * */
    public String getUuid(String token) {

        return Jwts.parser()
                   .verifyWith(getSigningKey())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload()
                   .getSubject();
    }
}
