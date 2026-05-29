package com.api.global.util;

import jakarta.servlet.http.HttpServletRequest;

public class HttpUtil {
    
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("CF-Connecting-IP"); // cloudflare 실제 클라이언트 아이피
        if (ip != null && !ip.isBlank()) return ip;

        ip = request.getHeader("X-Forwarded-For"); //프록시 체인 전체 아이피
        if (ip != null && !ip.isBlank()) return ip.split(",")[0].trim(); // 실사용자 아이피

        ip = request.getHeader("X-Real-IP"); //Nginx 설정 실아이피
        if (ip != null && !ip.isBlank()) return ip;

        return request.getRemoteAddr();
    }
}
