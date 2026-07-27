package com.api.domain.chatbot.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.api.global.common.ApiResponse;
import com.api.global.exception.BusinessException;
import com.api.global.util.chatbotUtil;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
@RequiredArgsConstructor
@Slf4j
public class chatbotController {
	
	private final chatbotUtil chatbotutil;
	
	@GetMapping("/chatbot")
	public ResponseEntity<ApiResponse<String>> memberDataLoad(@RequestParam(required = false) String content){
		
		if (content == null || content.isBlank()) {
			throw new BusinessException("내용은 필수 값입니다.");
        }

        String answer;
        try {
            answer = chatbotutil.ask(content);
        } catch (RestClientException e) {
            // 챗봇 컨테이너 다운/타임아웃 등 호출 실패
        	log.error("챗봇 호출 실패: ", e);
        	throw new BusinessException("챗봇 서버 호출에 실패하였습니다.");
        }
		
		return ResponseEntity.ok(ApiResponse.ok(answer));
	}
	
	@GetMapping("/chatbot/stream")
    public SseEmitter memberDataLoadStream(@RequestParam(required = false) String content) {

		if (content == null || content.isBlank()) {
            throw new BusinessException("내용은 필수 값입니다.");
        }

        SseEmitter emitter = new SseEmitter(TimeUnit.MINUTES.toMillis(10));

        ScheduledExecutorService heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
        heartbeatExecutor.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event().comment("heartbeat"));
            } catch (Exception e) {
                heartbeatExecutor.shutdown();
            }
        }, 10, 10, TimeUnit.SECONDS);

        CompletableFuture.supplyAsync(() -> chatbotutil.ask(content))
                .whenComplete((answer, error) -> {
                    heartbeatExecutor.shutdown();
                    try {
                        if (error != null) {
                            emitter.completeWithError(error);
                        } else {
                            emitter.send(answer);
                            emitter.complete();
                        }
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }
                });

        return emitter;
    }
}
