package com.api.domain.base.Member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequest {
	
	//유저 아이디
	@NotBlank(message="아이디는 필수값입니다.")
	private String userId;
	//비밀번호
	@NotBlank(message="비밀번호는 필수값입니다.")
	private String password;
	//직급
	private String rank;
	//회사명
	private String cpName;
	//이메일
	private String email;
	
}
