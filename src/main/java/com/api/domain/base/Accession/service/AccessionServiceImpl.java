package com.api.domain.base.Accession.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.domain.auth.entity.AuthEntity;
import com.api.domain.auth.entity.UserAuthEntity;
import com.api.domain.auth.repository.AuthRepository;
import com.api.domain.auth.repository.UserAuthRepository;
import com.api.domain.base.Accession.dto.AccessionRequest;
import com.api.domain.base.Member.entity.MemberEntity;
import com.api.domain.base.Member.repository.MemberRepository;
import com.api.global.constants.MessageConstants;
import com.api.global.exception.BusinessException;
import com.api.global.redis.RedisService;
import com.api.global.util.MailUtil;

import lombok.RequiredArgsConstructor;

/**
 * 회원가입(Accession) 서비스 구현체
 *
 * Redis를 활용한 이메일 인증 기반 회원가입 3단계 플로우를 구현합니다.
 *
 * [Redis 키 구조]
 *   SIGNUP_CERT:{email}     → 인증번호 (TTL 3분)
 *   SIGNUP_VERIFIED:{email} → 인증 완료 상태 (TTL 5분)
 *
 * [비밀번호 찾기와의 차이]
 *   비밀번호 찾기: 기존 회원의 UUID 기반으로 Redis 키 생성 (CERT:{uuid})
 *   회원가입:      회원이 아직 없으므로 이메일 기반으로 Redis 키 생성 (SIGNUP_CERT:{email})
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccessionServiceImpl implements AccessionService {

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailUtil mailUtil;
    private final RedisService redisService;

    /**
     * [1단계] 회원가입 이메일 인증번호 발송
     *
     * 100000 ~ 999999 사이의 6자리 난수를 생성하고
     * Redis의 SIGNUP_CERT:{email} 키에 3분간 저장한 뒤 이메일로 발송합니다.
     *
     * @param email 인증번호를 수신할 이메일 주소
     * @throws BusinessException SMTP 발송 실패 시 EMAIL_SEND_FAILED
     */
    @Override
    public void sendEmailAuth(String email) {
        // 6자리 난수 인증번호 생성 (100000 ~ 999999)
        String certNum = String.valueOf((int)(Math.random() * 900000) + 100000);

        // Redis에 인증번호 저장 (TTL 3분)
        redisService.saveSignupCertNum(email, certNum);

        // 이메일 발송 (실패 시 BusinessException 으로 변환)
        try {
            mailUtil.reqSignup(email, certNum);
        } catch (Exception e) {
            throw new BusinessException(MessageConstants.EMAIL_SEND_FAILED);
        }
    }

    /**
     * [2단계] 이메일 인증번호 확인
     *
     * Redis에서 SIGNUP_CERT:{email} 키를 조회해 입력값과 비교합니다.
     * 일치하면 SIGNUP_VERIFIED:{email} 키를 5분간 저장합니다.
     *
     * @param email   1단계에서 인증번호를 발송한 이메일
     * @param certNum 사용자가 입력한 6자리 인증번호
     * @throws BusinessException 인증번호 만료(CERT_NUM_EXPIRED) 또는 불일치(CERT_NUM_NOT_MATCH)
     */
    @Override
    public void verifyNum(String email, String certNum) {
        // Redis에서 저장된 인증번호 조회 (3분 TTL 초과 시 null 반환)
        String savedCertNum = redisService.getSignupCertNum(email);

        if (savedCertNum == null) {
            // 인증번호 TTL(3분) 초과로 자동 삭제된 경우
            throw new BusinessException(MessageConstants.CERT_NUM_EXPIRED);
        }
        if (!savedCertNum.equals(certNum)) {
            // 입력한 인증번호가 저장된 값과 다른 경우
            throw new BusinessException(MessageConstants.CERT_NUM_NOT_MATCH);
        }

        // 인증 완료 상태를 Redis에 저장 (TTL 5분 — 5분 내에 회원가입을 완료해야 함)
        redisService.saveSignupVerified(email);
    }

    /**
     * [3단계] 회원가입 처리
     *
     * 다음 순서로 처리됩니다.
     *   1. 아이디 중복 확인
     *   2. 이메일 인증 완료 여부 확인 (Redis SIGNUP_VERIFIED)
     *   3. 비밀번호 BCrypt 해싱
     *   4. UUID 생성 및 Member 테이블 INSERT
     *   5. USER_AUTH 테이블에 기본 권한 'USER' 부여
     *   6. Redis 인증 완료 키 삭제 (재사용 방지)
     *
     * @param request 회원가입 요청 DTO
     * @throws BusinessException 아이디 중복(USER_ID_DUPLICATED), 인증 미완료(CERT_NOT_VERIFIED),
     *                           권한코드 없음(AUTH_NOT_FOUND)
     */
    @Override
    @Transactional
    public void join(AccessionRequest request, String clientIp) {
        // 1. 아이디 중복 확인
        if (memberRepository.existsById(request.getUserId())) {
            throw new BusinessException(MessageConstants.USER_ID_DUPLICATED);
        }

        // 2. 이메일 인증 완료 여부 확인 (2단계를 거치지 않으면 가입 불가)
        if (!redisService.getSignupVerified(request.getEmail())) {
            throw new BusinessException(MessageConstants.CERT_NOT_VERIFIED);
        }

        // 3. 비밀번호 BCrypt 해싱 (평문 저장 방지)
        String encodedPassword = passwordEncoder.encode(request.getPassword());

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
                .password(encodedPassword)
                .socialYn("N")            // 일반 회원 (SSO/소셜 로그인 아님)
                .status("Y")              // 정상 활성 상태
                .createdDate(now)
                .lastLogin(now)           // 가입 시점을 최초 로그인 시각으로 기록
                .lastLoginAddress(clientIp) // 가입 시점 IP 기록
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
    }
}
