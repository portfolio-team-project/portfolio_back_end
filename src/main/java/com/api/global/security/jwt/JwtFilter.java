package com.api.global.security.jwt;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private final JwtProvider jwtProvider;
	
	@Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Authorization 헤더 가져오기
        String bearerToken = request.getHeader("Authorization");

        // Bearer 토큰인지 확인
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {

            // "Bearer " 제거
            String token = bearerToken.substring(7);

            // JWT 유효성 검사
            if (jwtProvider.validateToken(token)) {

                // JWT에서 username 추출
                String username = jwtProvider.getUsername(token);

                // 인증 객체 생성
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of()
                        );

                // SecurityContext에 등록
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }

        // 다음 필터로 넘기기
        filterChain.doFilter(request, response);
    }
}
