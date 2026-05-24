package com.api.domain.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.domain.auth.entity.UserAuthEntity;
import com.api.domain.base.Member.entity.MemberEntity;

public interface UserAuthRepository extends JpaRepository<UserAuthEntity, Long>{
	
	Optional<UserAuthEntity> findByUserId(MemberEntity member);
}
