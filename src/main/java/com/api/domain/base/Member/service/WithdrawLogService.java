package com.api.domain.base.Member.service;

import java.time.LocalDateTime;

public interface WithdrawLogService {
	
	boolean isRecentlyWithdrawn(String userId, LocalDateTime after);
}
