package com.api.domain.auth.service;

import com.api.domain.auth.entity.UserAuthEntity;
import com.api.domain.base.Member.entity.MemberEntity;

public interface AuthService {
	
	UserAuthEntity findByUserId(MemberEntity member);
}
