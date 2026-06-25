package com.api.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KaKaoProperties (
    String clientId,
    String redirectUri,
    String tokenUrl,
    String userInfoUrl
) {}
