package com.api.global.util;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class chatbotUtil {

    private final RestTemplate restTemplate;

    @Value("${chatbot.url}")
    private String chatbotUrl;

    @Value("${chatbot.token}")
    private String chatbotToken;

    public chatbotUtil(RestTemplateBuilder builder) {
        this.restTemplate = builder
                .requestFactory(SimpleClientHttpRequestFactory::new)   // JDK HttpClient 대신 HttpURLConnection 기반으로 고정
                .connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(180))
                .additionalInterceptors((request, body, execution) -> {
                    log.info("=== OUTGOING REQUEST ===");
                    log.info("URI: {}", request.getURI());
                    log.info("Method: {}", request.getMethod());
                    log.info("Headers: {}", request.getHeaders());
                    log.info("Body bytes length: {}", body.length);
                    log.info("Body content: {}", new String(body, StandardCharsets.UTF_8));
                    return execution.execute(request, body);
                })
                .build();
    }

    public String ask(String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Token", chatbotToken);
        
        query = HtmlSanitizer.sanitize(query);

        Map<String, String> body = Map.of("query", query);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<ChatResponse> response =
                restTemplate.postForEntity(chatbotUrl, entity, ChatResponse.class);

        return response.getBody().answer();
    }


    public record ChatRequest(String query) {}
    public record ChatResponse(String answer) {}
}
