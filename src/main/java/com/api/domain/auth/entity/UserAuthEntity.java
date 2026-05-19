package com.api.domain.auth.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER_AUTH")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuthEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auth_seq")
	private Long authSeq;
	
	@ManyToOne
	@JoinColumn(name="auth_code")
	private AuthEntity auth;
	
	@Column(name = "user_nm", length = 10)
	private String userNm;
	
	@Column(name = "reg_dt")
	private LocalDateTime regDt;
	
	@Column(name = "modi_dt")
	private LocalDateTime modiDt;
}
