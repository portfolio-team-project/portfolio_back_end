package com.api.domain.base.Member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.domain.base.Member.dto.MemberResponse;
import com.api.domain.base.Member.entity.MemberEntity;

public interface MemberService {
    
	String checkUserId(String userId);
	MemberEntity login(String userId, String password);
	String getRole(MemberEntity member);
	MemberEntity findByUuid(String uuid);
	void sendCertificationEmail(String userId, String email);
	void verifyCertificationNum(String userId,String certNum);
	void changePassword(String userId, String newPassword);
	void checkPasswordExpired(MemberEntity member);
	void verifyAndChangePassword(String userId, String currentPwd, String newPassword);
	Page<MemberResponse> findAllMembers(Pageable pageable,String keyword, String searchType);
	void withdraw(String userId, String password);
	MemberEntity findByUserId(String userId);
	void socialWithdraw(String uuid);
}
