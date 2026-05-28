package com.api.domain.base.Member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.domain.base.Member.entity.MemberEntity;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String>{
	Optional<MemberEntity> findByUuid(String uuid);
	Optional<MemberEntity> sendCertificationEmail(String userId, String email);
	
}
