package com.api.global.util;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

@Component
@Slf4j
public class chatbotUtil {

    private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${chatbot.url}")
    private String chatbotUrl;

    @Value("${chatbot.token}")
    private String chatbotToken;
    
    @Value("${chatbot.stream}")
    private String chatbotStreamUrl;

    public chatbotUtil(RestTemplateBuilder builder,
                        WebClient.Builder webClientBuilder,
                        ObjectMapper objectMapper,
                        @Value("${chatbot.stream}") String chatbotStreamUrl) {
        this.restTemplate = builder
                .requestFactory(SimpleClientHttpRequestFactory::new)
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

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMinutes(5))
                .keepAlive(false);

        this.webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        this.objectMapper = objectMapper;
        this.chatbotStreamUrl = chatbotStreamUrl;
    }

    public String ask(String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Token", chatbotToken);

        String sanitized = HtmlSanitizer.sanitize(query);

        Map<String, String> body = Map.of("query", sanitized);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<ChatResponse> response =
                restTemplate.postForEntity(chatbotUrl, entity, ChatResponse.class);

        return response.getBody().answer();
    }

    public Flux<String> askStream(String query) {
        String sanitized = HtmlSanitizer.sanitize(query);

        return webClient.post()
                .uri(chatbotStreamUrl)
                .header("X-Token", chatbotToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("query", sanitized))
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {})
                .filter(sse -> sse.data() != null && !"done".equals(sse.event()))
                .map(sse -> unwrapJsonString(sse.data()))
                .doOnError(e -> log.error("챗봇 스트리밍 호출 실패", e));
    }

    private String unwrapJsonString(String rawData) {
        try {
            return objectMapper.readValue(rawData, String.class);
        } catch (Exception e) {
            return rawData;
        }
    }

    public record ChatRequest(String query) {}
    public record ChatResponse(String answer) {}
}
