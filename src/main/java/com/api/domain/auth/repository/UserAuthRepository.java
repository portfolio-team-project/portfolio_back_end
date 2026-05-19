package com.api.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.domain.auth.entity.UserAuthEntity;

public interface UserAuthRepository extends JpaRepository<UserAuthEntity, Long>{

}
