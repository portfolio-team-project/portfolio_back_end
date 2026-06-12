package com.api.domain.base.Member.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberResponse {
		//유저 아이디
		private String userId;
		//유저 이름
		private String userName;
		//비밀번호
		private String password;
		//직급
		private String status;
		//회사명
		private String cpName;
		//이메일
		private String email;
		//생성일
		private LocalDateTime createdDate;

}
