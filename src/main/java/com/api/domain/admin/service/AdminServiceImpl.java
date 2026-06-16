package com.api.domain.admin.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.api.domain.base.Member.dto.MemberDetailResponse;
import com.api.domain.base.Member.entity.MemberEntity;
import com.api.domain.base.Member.repository.MemberRepository;
import com.api.global.constants.MessageConstants;
import com.api.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final MemberRepository memberRepository;
	
	@Override
	public long countThisMonthMembers() {
		
		LocalDateTime st = LocalDate.now().withDayOfMonth(1).atStartOfDay();
		LocalDateTime ed = LocalDateTime.now();
		
		return memberRepository.countByCreatedDateBetween(st,ed);
	}

	@Override
	public MemberDetailResponse findMemberDetail(String userId) {
		MemberEntity member = memberRepository.findByUserId(userId)
				                              .orElseThrow( () -> new BusinessException(MessageConstants.MEMBER_INFO_NOT_FOUND) );
		
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

}
