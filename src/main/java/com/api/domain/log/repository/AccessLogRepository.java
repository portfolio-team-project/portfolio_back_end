package com.api.domain.log.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.domain.log.entity.AccessLogEntity;

public interface AccessLogRepository extends JpaRepository<AccessLogEntity, Long>{
	
}
