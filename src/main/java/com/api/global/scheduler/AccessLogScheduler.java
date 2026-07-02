package com.api.global.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.api.domain.log.service.AccessLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccessLogScheduler {
	
	private final AccessLogService accessLogService;
	
	@Scheduled(cron = "0 0 3 1 * *")  // 매월 1일 새벽 3시
    public void deleteOldLogs() {
		
		accessLogService.deleteBeforeSixMonth();
        log.info("[SCHEDULER] 6개월 이전 접속 로그 삭제 완료");
    }
}
