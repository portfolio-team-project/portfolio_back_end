package com.api.domain.auth.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AUTH")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthEntity {
	
	@Id
	@Column(name = "auth_code", length = 50)
	private String authCode;
	
	@Column(name = "auth_nm", length = 50)
	private String authNm;
	
	@Column(name = "reg_dt")
	private LocalDateTime regDt;
	
	@Column(name = "use_yn", length = 1)
	private String useYn;
}
