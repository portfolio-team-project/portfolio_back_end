package com.api.domain.log.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.domain.log.entity.AccessLogEntity;
import com.api.domain.log.repository.AccessLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccessLogServiceImpl implements AccessLogService{
	
	private final AccessLogRepository accessLogRepository;
	
	@Override
	@Transactional
	public void accessLogSave(String accessIp, String accessUri, String method) {
		
		accessLogRepository.save(AccessLogEntity.builder()
				                                .accessIp(accessIp)
				                                .accessUri(accessUri)
				                                .method(method)
				                                .accessDt(LocalDateTime.now())
				                                .build()
				);
	}
	
}
