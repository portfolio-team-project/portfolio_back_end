package com.api.domain.base.Member.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.domain.auth.entity.UserAuthEntity;
import com.api.domain.auth.repository.UserAuthRepository;
import com.api.domain.base.Member.entity.FindPasswordEntity;
import com.api.domain.base.Member.entity.MemberEntity;
import com.api.domain.base.Member.repository.FindPasswordRepository;
import com.api.domain.base.Member.repository.MemberRepository;
import com.api.global.exception.BusinessException;
import com.api.global.redis.RedisService;
import com.api.global.util.MailUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {
	
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserAuthRepository userAuthRepository;
	private final FindPasswordRepository findPasswordRepository;
	private final MailUtil mailUtil;
	private final RedisService redisService;

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

    @Override
    public void sendCertificationEmail(String userId, String email) {
        MemberEntity member = memberRepository.sendCertificationEmail(userId, email)
                                              .orElseThrow(() -> new BusinessException("정보가 존재하지 않습니다."));
        
        //임의의 난수값 생성
        String certNum = String.valueOf((int)(Math.random() * 900000) + 100000);
       
       redisService.saveCertNum(member.getUuid(), certNum);
       
       try {
           mailUtil.req(email,certNum);
        } catch (Exception e) {
            throw new BusinessException("이메일 발송에 실패했습니다.");
        }
    }

    @Override
    public void verifyCertificationNum(String userId, String certNum) {
        
        FindPasswordEntity findPassword = findPasswordRepository.findByMember_UserId(userId)
                                                                .orElseThrow(() -> new BusinessException("존재하지 않는 유저입니다."));
        
        String savedCertNum = redisService.getCertNum(findPassword.getMember().getUuid());
        
        if (savedCertNum == null) {
            throw new BusinessException("인증번호가 만료되었습니다.");
        }
        if (!savedCertNum.equals(certNum)) {
            throw new BusinessException("인증번호가 일치하지 않습니다.");
        }
    }
	
	

}
