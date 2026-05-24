package com.api.domain.auth.service;

import org.springframework.stereotype.Service;

import com.api.domain.auth.dto.AuthResponse;
import com.api.domain.auth.entity.AuthEntity;
import com.api.domain.auth.entity.UserAuthEntity;
import com.api.domain.auth.repository.AuthRepository;
import com.api.domain.auth.repository.UserAuthRepository;
import com.api.domain.base.Member.entity.MemberEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
	
	private final UserAuthRepository userAuthRepository;

	@Override
	public UserAuthEntity findByUserId(MemberEntity member) {
		
		UserAuthEntity userAuth = userAuthRepository.findByUserId(member).orElse(null);
		
		return userAuth;
	}

}
