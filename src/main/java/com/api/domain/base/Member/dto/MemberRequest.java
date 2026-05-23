package com.api.domain.base.Member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequest {
	
	//유저 아이디
	private String userId;
	//비밀번호
	private String password;
	//직급
	private String rank;
	//회사명
	private String cpName;
	//이메일
	private String email;
	
}
