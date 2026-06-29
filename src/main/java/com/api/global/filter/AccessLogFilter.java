package com.api.global.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.api.domain.log.service.AccessLogService;
import com.api.global.util.HttpUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccessLogFilter implements Filter{
	
	private final AccessLogService accessLogService;
	
	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String ip = HttpUtil.getClientIp(httpRequest);
        String uri = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        log.info("[ACCESS] IP={} method={} uri={}", ip, method, uri);
        accessLogService.accessLogSave(ip, uri, method);

        chain.doFilter(request, response);
    }
}
