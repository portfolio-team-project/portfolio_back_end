package com.api.domain.base.Member.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.domain.auth.entity.UserAuthEntity;
import com.api.domain.auth.repository.UserAuthRepository;
import com.api.domain.base.Member.entity.MemberEntity;
import com.api.domain.base.Member.repository.MemberRepository;
import com.api.global.constants.MessageConstants;
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
	private final MailUtil mailUtil;
	private final RedisService redisService;

	@Override
	public boolean existsByUserId(String userId) {
		return memberRepository.existsById(userId);
	}

	@Override
	public MemberEntity login(String userId, String password) {
		MemberEntity member = memberRepository.findById(userId).orElseThrow(() -> new BusinessException(MessageConstants.MEMBER_NOT_FOUND));
		
		if (!passwordEncoder.matches(password, member.getPassword())) {
			throw new BusinessException(MessageConstants.PASSWORD_NOT_MATCH);
		}
		
		return member;
	}

	@Override
	public String getRole(MemberEntity member) {
		UserAuthEntity userAuth = userAuthRepository.findByUserId(member)
	            .orElseThrow(() -> new BusinessException(MessageConstants.AUTH_NOT_FOUND));
		
		return userAuth.getAuth().getAuthNm();
	}

	@Override
	public MemberEntity findByUuid(String uuid) {
		return memberRepository.findByUuid(uuid)
	            .orElseThrow(() -> new BusinessException(MessageConstants.UUID_NOT_FOUND));
	}

    @Override
    public void sendCertificationEmail(String userId, String email) {
        MemberEntity member = memberRepository.findByUserIdAndEmail(userId, email)
                                              .orElseThrow(() -> new BusinessException(MessageConstants.CHECK_EMAIL_ID));
        
        //임의의 난수값 생성
        String certNum = String.valueOf((int)(Math.random() * 900000) + 100000);
       
       redisService.saveCertNum(member.getUuid(), certNum);
       
       try {
           mailUtil.req(email,certNum);
        } catch (Exception e) {
            throw new BusinessException(MessageConstants.EMAIL_SEND_FAILED);
        }
    }

    @Override
    public void verifyCertificationNum(String userId, String certNum) {
        
        MemberEntity member = memberRepository.findByUserId(userId)
                                                .orElseThrow(() -> new BusinessException(MessageConstants.MEMBER_NOT_FOUND));
        
        String savedCertNum = redisService.getCertNum(member.getUuid());
        
        if (savedCertNum == null) {
            throw new BusinessException(MessageConstants.CERT_NUM_EXPIRED);
        }
        if (!savedCertNum.equals(certNum)) {
            throw new BusinessException(MessageConstants.CERT_NUM_NOT_MATCH);
        }
        
        redisService.saveVerified(member.getUuid());
    }

    @Override
    @Transactional
    public void changePassword(String userId, String newPassword) {
        MemberEntity member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(MessageConstants.MEMBER_NOT_FOUND));
        
        if (!redisService.getVerified(member.getUuid())) {
            throw new BusinessException(MessageConstants.CERT_NOT_VERIFIED);
        }
        
        updatePassword(member, newPassword);
        
        redisService.deleteVerified(member.getUuid());
    }

    @Override
    public void checkPasswordExpired(MemberEntity member) {
        
        if (member.getChgPwdDt() == null || 
                member.getChgPwdDt().isBefore(LocalDateTime.now().minusMonths(3))) {
            throw new BusinessException(MessageConstants.PWD_EXPIRED);
            }
    }

    @Override
    @Transactional
    public void verifyAndChangePassword(String userId, String currentPwd, String newPassword) {
        MemberEntity member = memberRepository.findById(userId).orElseThrow(() -> new BusinessException(MessageConstants.MEMBER_NOT_FOUND));
        
        if (!passwordEncoder.matches(currentPwd, member.getPassword())) {
            
            throw new BusinessException(MessageConstants.PASSWORD_NOT_MATCH);
        }
        
        updatePassword(member, newPassword);
        
        redisService.deleteRefreshToken(member.getUuid());
        
    }
    
    private void updatePassword(MemberEntity member, String newPassword) {
        member.updatePassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
    }
	
	

}
