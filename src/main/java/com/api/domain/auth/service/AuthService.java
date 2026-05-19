package com.api.domain.auth.service;

import com.api.domain.auth.dto.AuthResponse;

public interface AuthService {
	
	AuthResponse findByCode(String AuthCode);
}
