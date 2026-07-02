package com.api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.api.global.config.KaKaoProperties;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(KaKaoProperties.class)
public class ApiApplication {
    public static void main(String[] args) {
        
        SpringApplication.run(ApiApplication.class, args);
    }
}
