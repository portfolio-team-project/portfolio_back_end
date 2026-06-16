package com.api.domain.admin.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.api.domain.base.Member.repository.MemberRepository;

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

}
