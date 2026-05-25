package com.api.domain.base.Member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.domain.auth.entity.UserAuthEntity;
import com.api.domain.auth.repository.UserAuthRepository;
import com.api.domain.base.Member.entity.MemberEntity;
import com.api.domain.base.Member.repository.MemberRepository;
import com.api.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
		MemberEntity member = memberRepository.findById(userId).orElseThrow(() -> new BusinessException("존재하지 않는 아이디입니다."));
		
		if (!passwordEncoder.matches(password, member.getPassword())) {
			throw new BusinessException("비밀번호가 일치하지 않습니다.");
		}
		
		return member;
	}

	@Override
	public String getRole(MemberEntity member) {
		UserAuthEntity userAuth = userAuthRepository.findByUserId(member)
	            .orElseThrow(() -> new BusinessException("권한 정보 없음"));
		
		return userAuth.getAuth().getAuthNm();
	}

	@Override
	public MemberEntity findByUuid(String uuid) {
		return memberRepository.findByUuid(uuid)
	            .orElseThrow(() -> new BusinessException("존재하지 않는 유저 식별번호입니다."));
	}
	
	

}
