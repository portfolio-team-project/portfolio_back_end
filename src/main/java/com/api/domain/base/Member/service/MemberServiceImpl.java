package com.api.domain.base.Member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.domain.auth.entity.UserAuthEntity;
import com.api.domain.auth.repository.UserAuthRepository;
import com.api.domain.base.Member.entity.MemberEntity;
import com.api.domain.base.Member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserAuthRepository userAuthRepository;

	@Override
	public boolean existsByUserId(String userId) {
		return memberRepository.existsById(userId);
	}

	@Override
	public MemberEntity login(String userId, String password) {
		MemberEntity member = memberRepository.findById(userId).orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));
		
		if (!passwordEncoder.matches(password, member.getPassword())) {
			throw new RuntimeException("비밀번호가 일치하지 않습니다.");
		}
		
		return member;
	}

	@Override
	public String getRole(MemberEntity member) {
		UserAuthEntity userAuth = userAuthRepository.findByUserId(member)
	            .orElseThrow(() -> new RuntimeException("권한 정보 없음"));
		
		return userAuth.getAuth().getAuthNm();
	}

	@Override
	public MemberEntity findByUserId(String userId) {
		return memberRepository.findByUserId(userId)
	            .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
	}
	
	

}
