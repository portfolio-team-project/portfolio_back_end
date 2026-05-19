package com.api.domain.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthRequest {
	
	private String authCode;
}
