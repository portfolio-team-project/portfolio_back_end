package com.api.domain.base.Member.service;

import com.api.domain.base.Member.entity.MemberEntity;

public interface MemberService {
	boolean existsByUserId(String userId);
	MemberEntity login(String userId, String password);
	String getRole(MemberEntity member);
	MemberEntity findByUuid(String uuid);
}
