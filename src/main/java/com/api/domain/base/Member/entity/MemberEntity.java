package com.api.domain.base.Member.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
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
	
	@Column(name="social_yn", length = 1)
	@Default
	private String socialYn = "N";
	
	@Column(name="kakao_id", length= 255)
	private String kakaoId;
	
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
	
	@Column(name="chg_pwd_dt")
	private LocalDateTime chgPwdDt;
	
	public void updatePassword(String password) {
	    this.password = password;
	}
}
