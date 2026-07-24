package com.api.domain.chatbot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.api.global.common.ApiResponse;
import com.api.global.exception.BusinessException;
import com.api.global.util.chatbotUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class chatbotController {
	
	private final chatbotUtil chatbotutil;
	
	@GetMapping("/chatbot")
	@PreAuthorize("hasAnyRole('USER')")
	public ResponseEntity<ApiResponse<String>> memberDataLoad(@RequestParam(required = false) String content){
		
		if (content == null || content.isBlank()) {
			throw new BusinessException("내용은 필수 값입니다.");
        }

        String answer;
        try {
            answer = chatbotutil.ask(content);
        } catch (RestClientException e) {
            // 챗봇 컨테이너 다운/타임아웃 등 호출 실패
        	throw new BusinessException("챗봇 서버 호출에 실패하였습니다.");
        }
		
		return ResponseEntity.ok(ApiResponse.ok(answer));
	}
}
