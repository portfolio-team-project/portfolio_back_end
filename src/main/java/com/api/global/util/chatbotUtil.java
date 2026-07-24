package com.api.global.util;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class chatbotUtil {

    private final RestTemplate restTemplate;

    @Value("${chatbot.url}")
    private String chatbotUrl;

    @Value("${chatbot.token}")
    private String chatbotToken;

    public chatbotUtil(RestTemplateBuilder builder) {
        this.restTemplate = builder
        		.connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(60))
                .build();
    }

    public String ask(String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Token", chatbotToken);

        String rawBody = "{\"query\":\"" + query + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(rawBody, headers);

        ResponseEntity<ChatResponse> response =
                restTemplate.postForEntity(chatbotUrl, entity, ChatResponse.class);

        return response.getBody().answer();
    }

    public record ChatRequest(String query) {}
    public record ChatResponse(String answer) {}
}
