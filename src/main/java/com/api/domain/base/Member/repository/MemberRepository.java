package com.api.domain.base.Member.repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.domain.base.Member.entity.MemberEntity;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String>{
	Optional<MemberEntity> findByUuid(String uuid);
	Optional<MemberEntity> findByUserIdAndEmail(String userId, String email);
	Optional<MemberEntity> findByUserId(String userId);
	Optional<MemberEntity> findByKakaoId(String kakaoId);
	long countByCreatedDateBetween(LocalDateTime start, LocalDateTime end);
	Page<MemberEntity> findByUserIdContaining(String userId, Pageable pageable);
	Page<MemberEntity> findByUserNameContaining(String userName, Pageable pageable);
	Page<MemberEntity> findByEmailContaining(String email, Pageable pageable);
}
