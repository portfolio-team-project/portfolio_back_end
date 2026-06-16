package com.api.domain.base.Login.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.domain.auth.entity.AuthEntity;
import com.api.domain.auth.entity.UserAuthEntity;
import com.api.domain.auth.repository.AuthRepository;
import com.api.domain.auth.repository.UserAuthRepository;
import com.api.domain.base.Login.dto.KakaoLoginRequest;
import com.api.domain.base.Login.entity.LoginEntity;
import com.api.domain.base.Login.repository.LoginRepository;
import com.api.domain.base.Member.entity.MemberEntity;
import com.api.domain.base.Member.repository.MemberRepository;
import com.api.global.constants.MessageConstants;
import com.api.global.exception.BusinessException;
import com.api.global.redis.RedisService;
import com.api.global.util.HttpUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginServiceImpl implements LoginService{
    
    private final LoginRepository loginRepository;
    private final MemberRepository memberRepository;
    private final RedisService redisService;
    private final AuthRepository authRepository;
    private final UserAuthRepository userAuthRepository;

    @Override
    @Transactional
    public void saveLog(MemberEntity member, HttpServletRequest request, String loginYn, String failReason) {
        
        String clientIp = HttpUtil.getClientIp(request);
        
        LoginEntity loginLog = LoginEntity.builder()
                                       .member(member)
                                       .userIp(clientIp)
                                       .requestUri(request.getRequestURI())
                                       .userAgent(request.getHeader("User-Agent"))
                                       .loginYn(loginYn)
                                       .failReason(failReason)
                                       .build();
        
        loginRepository.save(loginLog);
    }

	@Override
	@Transactional
	public MemberEntity kakaoJoin(KakaoLoginRequest request, HttpServletRequest httpRequest) {
		
		// 1. 아이디 중복 확인
        memberRepository.findById(request.getUserId()).ifPresent(m -> {
            if ("N".equals(m.getStatus())) {
                throw new BusinessException(MessageConstants.MEMBER_WITHDRAWN);
            }
            throw new BusinessException(MessageConstants.USER_ID_DUPLICATED);
        });

        // 2. 이메일 인증 완료 여부 확인 (2단계를 거치지 않으면 가입 불가)
        if (!redisService.getSignupVerified(request.getEmail())) {
            throw new BusinessException(MessageConstants.CERT_NOT_VERIFIED);
        }
        
        // 3. client Id 생성
        String clientIp = HttpUtil.getClientIp(httpRequest);

        // 4. UUID 생성 — JWT 및 Redis 키에서 회원을 식별하는 값
        String uuid = UUID.randomUUID().toString();

        // 5. Member 엔티티 생성 및 저장
        LocalDateTime now = LocalDateTime.now();
        MemberEntity member = MemberEntity.builder()
                .userId(request.getUserId())
                .uuid(uuid)
                .userName(request.getUserName())
                .rank(request.getRank())
                .cpName(request.getCpName())
                .email(request.getEmail())
                .kakaoId(request.getKakaoId())
                .status("Y")              // 정상 활성 상태
                .createdDate(now)
                .lastLogin(now)
                .lastLoginAddress(clientIp)
                .chgPwdDt(now)
                .work(request.getWork())
                .department(request.getDepartment())
                .termsAgree(request.isTermsAgree())
                .privacyAgree(request.isPrivacyAgree())
                .marketingAgree(request.isMarketingAgree())
                .termsAgreeDate(now)
                .privacyAgreeDate(now)
                .marketingAgreeDate(request.isMarketingAgree() ? now : null)
                .build();

        MemberEntity savedMember = memberRepository.save(member);

        // 6. 기본 권한 조회 — AUTH 테이블의 auth_code 값이 "USER"여야 합니다.
        //    DB에서 실제 값을 확인하고 다를 경우 이 부분을 수정하세요.
        AuthEntity auth = authRepository.findById("USER")
                .orElseThrow(() -> new BusinessException(MessageConstants.AUTH_NOT_FOUND));

        // 7. USER_AUTH 테이블에 회원-권한 매핑 저장
        UserAuthEntity userAuth = UserAuthEntity.builder()
                .auth(auth)
                .userId(savedMember)
                .userNm(request.getUserName())
                .regDt(LocalDateTime.now())
                .build();

        userAuthRepository.save(userAuth);

        // 8. 인증 완료 키 삭제 (같은 이메일로 재가입 시도 방지)
        redisService.deleteSignupVerified(request.getEmail());
        
        return member;
	}
    
}
