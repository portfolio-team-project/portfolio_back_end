package com.api.domain.base.Member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.domain.base.Member.entity.FindPasswordEntity;

public interface FindPasswordRepository extends JpaRepository<FindPasswordEntity, Long>{
    
}
