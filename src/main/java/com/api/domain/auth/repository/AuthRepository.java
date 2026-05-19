package com.api.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.domain.auth.entity.AuthEntity;


public interface AuthRepository extends JpaRepository<AuthEntity, String>{
	
	
}
