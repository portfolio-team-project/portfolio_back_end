package com.api.domain.auth.service;

import org.springframework.stereotype.Service;

import com.api.domain.auth.dto.AuthResponse;
import com.api.domain.auth.entity.AuthEntity;
import com.api.domain.auth.repository.AuthRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
	
	private final AuthRepository authRepository;

	@Override
	public AuthResponse findByCode(String AuthCode) {
		
		AuthEntity auth = authRepository.findById(AuthCode).orElse(null);
		
		return AuthResponse.builder()
				           .authNm(auth.getAuthNm())
				           .build();
	}

}
