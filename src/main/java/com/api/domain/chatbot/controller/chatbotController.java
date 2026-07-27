package com.api.domain.chatbot.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.api.global.common.ApiResponse;
import com.api.global.exception.BusinessException;
import com.api.global.redis.RedisService;
import com.api.global.util.UuidUtil;
import com.api.global.util.chatbotUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
@RequiredArgsConstructor
@Slf4j
public class chatbotController {
	
	private final chatbotUtil chatbotutil;
	private final RedisService redisService;
	
	@GetMapping("/chatbot")
	public ResponseEntity<ApiResponse<Map<String, String>>> memberDataLoad(@RequestParam(required = false) String content){
		
		if (content == null || content.isBlank()) {
			throw new BusinessException("내용은 필수 값입니다.");
        }

        String jobId = UuidUtil.makeUuid();
        redisService.saveChatStatus(jobId, "processing");
        
        CompletableFuture.supplyAsync(() -> chatbotutil.ask(content))
        .whenComplete((answer, error) -> {
            if (error != null) {
                log.error("챗봇 호출 실패", error);
                redisService.saveChatAnswer(jobId, "챗봇 서버 호출에 실패하였습니다.");
                redisService.saveChatStatus(jobId, "error");
            } else {
                redisService.saveChatAnswer(jobId, answer);
                redisService.saveChatStatus(jobId, "done");
            }
        });
		
        return ResponseEntity.ok(ApiResponse.ok(Map.of("jobId", jobId)));
	}
	
	@GetMapping("/chatbot/{jobId}")
    public ResponseEntity<ApiResponse<Map<String, String>>> getResult(@PathVariable String jobId) {
        String status = redisService.getChatStatus(jobId);
        if (status == null) {
            throw new BusinessException("존재하지 않거나 만료된 요청입니다.");
        }

        Map<String, String> result = new HashMap<>();
        result.put("status", status);

        if (!"processing".equals(status)) {
            result.put("answer", redisService.getChatAnswer(jobId));
            redisService.deleteChatJob(jobId);
        }

        return ResponseEntity.ok(ApiResponse.ok(result));
    }
}
