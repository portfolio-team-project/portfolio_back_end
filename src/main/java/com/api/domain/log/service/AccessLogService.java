package com.api.domain.log.service;

public interface AccessLogService {
	
	void accessLogSave(String accessIp, String accessUri, String method);
	void deleteBeforeSixMonth();
}
