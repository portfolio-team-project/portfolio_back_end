package com.api.domain.admin.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.domain.base.Member.dto.MemberDetailResponse;
import com.api.domain.base.Member.entity.MemberEntity;
import com.api.domain.base.Member.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public long countThisMonthMembers() {
		
		LocalDateTime st = LocalDate.now().withDayOfMonth(1).atStartOfDay();
		LocalDateTime ed = LocalDateTime.now();
		
		return memberService.countByCreatedDateBetween(st,ed);
	}

	@Override
	public MemberDetailResponse findMemberDetail(String userId) {
		MemberEntity member = memberService.findByUserId(userId);
		
		return MemberDetailResponse.builder()
				                   .userId(member.getUserId())
				                   .userName(member.getUserName())
				                   .email(member.getEmail())
				                   .status(member.getStatus())
				                   .createdDate(member.getCreatedDate())
				                   .cpName(member.getCpName())
				                   .rank(member.getRank())
				                   .department(member.getDepartment())
				                   .work(member.getWork())
				                   .build();
	}

	@Override
	public void deleteUserId(String userId) {
		
		MemberEntity member = memberService.findByUserId(userId);
		
		if ( member.getKakaoId() != null && !member.getKakaoId().isEmpty() ) {
			memberService.socialWithdraw(member.getUuid());
		}
		else {
			memberService.adminWithdraw(member.getUuid());
		}
		
	}

}
