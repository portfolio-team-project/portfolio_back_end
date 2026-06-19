package com.api.domain.base.Login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginResponse {
	
	private String accessToken;
	private String userId;
	private String userName;
	private String role;
	
	@JsonProperty("isSocial")
	private boolean isSocial;
}
