package com.api.domain.base.Member.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.api.domain.auth.entity.UserAuthEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	
	@Column(name="kakao_id", length= 255)
	private String kakaoId;
	
	@Column(name="email", length = 30)
	private String email;
	
	@Column(name="password", length = 255)
	@JsonIgnore
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

	@Column(name="work", length = 50)
	private String work;

	@Column(name="department", length = 50)
	private String department;

	@Column(name="terms_agree", nullable = false)
	private boolean termsAgree;

	@Column(name="privacy_agree", nullable = false)
	private boolean privacyAgree;

	@Column(name="marketing_agree", nullable = false)
	private boolean marketingAgree;

	@Column(name="terms_agree_date")
	private LocalDateTime termsAgreeDate;

	@Column(name="privacy_agree_date")
	private LocalDateTime privacyAgreeDate;

	@Column(name="marketing_agree_date")
	private LocalDateTime marketingAgreeDate;
	
	@Column(name="temp_pwd_yn", length = 1)
	@ColumnDefault("'N'")
	private String tempPwdYn;
	
	//cascade 권한 삭제
	@OneToMany(mappedBy = "userId", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<UserAuthEntity> userAuthList;


	public void updatePassword(String password) {
	    this.password = password;
	    this.tempPwdYn = "N";
	    this.chgPwdDt = LocalDateTime.now();
	}

	public void withdraw() {
	    this.status = "N";
	    this.updatedDate = LocalDateTime.now();
	}
	
	public void updateTempPwd(String password) {
		this.tempPwdYn = "Y";
		this.password = password;
		this.chgPwdDt = LocalDateTime.now();
	}
}
