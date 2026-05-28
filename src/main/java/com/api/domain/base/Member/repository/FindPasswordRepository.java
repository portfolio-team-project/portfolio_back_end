package com.api.domain.base.Member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.domain.base.Member.entity.FindPasswordEntity;
import com.api.domain.base.Member.entity.MemberEntity;

public interface FindPasswordRepository extends JpaRepository<FindPasswordEntity, Long>{
    void deleteByMember(MemberEntity member);
    Optional<FindPasswordEntity> findByMember_UserId(String userId); 
}
