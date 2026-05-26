package com.api.domain.base.Member.entity;

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
@Table(name="Member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberEntity {
	@Id
	@Column(name="user_id", length = 10)
	private String userId;
	
	@Column(name="uuid")
	private String uuid;
	
	@Column(name="user_name", length = 10)
	private String userName;
	
	@Column(name="rank", length = 10)
	private String rank;
	
	@Column(name="cp_name", length = 10)
	private String cpName;
	
	@Column(name="sso", length = 10)
	private String sso;
	
	@Column(name="email", length = 30)
	private String email;
	
	@Column(name="password", length = 255)
	private String password;
	
	@Column(name="status", length = 5)
	private String status;
	
	@Column(name="created_date")
	private LocalDateTime createdDate;
	
	@Column(name="updated_date")
	private LocalDateTime updatedDate;
	
	@Column(name="last_login")
	private LocalDateTime lastLogin;
	
	@Column(name="last_login_address", length=30)
	private String lastLoginAddress;
}
