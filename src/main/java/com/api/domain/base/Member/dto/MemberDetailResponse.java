package com.api.domain.base.Member.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberDetailResponse {
	private String userId;
    private String userName;
    private String email;
    private String status;
    private LocalDateTime createdDate;
    private String cpName;
    private String rank;
    private String department;
    private String work;
}
