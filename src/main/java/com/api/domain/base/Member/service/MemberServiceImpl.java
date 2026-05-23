package com.api.domain.base.Member.service;

import org.springframework.stereotype.Service;

import com.api.domain.base.Member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	private final MemberRepository memberRepository;

	@Override
	public boolean existsByUserId(String userId) {
		return memberRepository.existsById(userId);
	}

}
