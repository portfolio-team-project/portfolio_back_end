package com.api.domain.admin.service;

import com.api.domain.base.Member.dto.MemberDetailResponse;

public interface AdminService {
	long countThisMonthMembers();
	MemberDetailResponse findMemberDetail(String userId);
}
