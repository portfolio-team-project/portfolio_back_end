package com.api.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;

@Getter
@ConfigurationProperties(prefix ="kakao")
public class KaKaoProperties {
    private String clientId;
    private String redirectUri;
    private String tokenUrl;
    private String userInfoUrl;
}
