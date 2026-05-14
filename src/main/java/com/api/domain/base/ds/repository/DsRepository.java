package com.api.domain.base.ds.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.domain.base.ds.entity.DS;

public interface DsRepository extends JpaRepository<DS, Integer>{
    
}
