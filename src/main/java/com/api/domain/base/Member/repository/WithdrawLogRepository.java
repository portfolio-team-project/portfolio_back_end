package com.api.domain.base.Member.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.domain.base.Member.entity.WithdrawLogEntity;

@Repository
public interface WithdrawLogRepository extends JpaRepository<WithdrawLogEntity, Long>{
	boolean existsByUserIdAndDeleteDtAfter(String userId, LocalDateTime after);
}
