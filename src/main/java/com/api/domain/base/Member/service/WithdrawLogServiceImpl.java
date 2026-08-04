package com.api.domain.base.Member.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.api.domain.base.Member.repository.WithdrawLogRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WithdrawLogServiceImpl implements WithdrawLogService {
	
	private WithdrawLogRepository withdrawLogRepository;

	@Override
	public boolean isRecentlyWithdrawn(String userId, LocalDateTime after) {
		return withdrawLogRepository.existsByUserIdAndDeleteDtAfter(userId, after);
	}

}
