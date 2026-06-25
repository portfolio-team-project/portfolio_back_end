package com.api.domain.base.Accession.service;

import com.api.domain.base.Accession.dto.AccessionRequest;

/**
 * 회원가입(Accession) 서비스 인터페이스
 *
 * 회원가입 3단계 플로우를 정의합니다.
 *   1. sendEmailAuth  — 이메일 인증번호 발송
 *   2. verifyNum      — 인증번호 확인
 *   3. join           — 회원 정보 저장
 */
public interface AccessionService {

    /**
     * [1단계] 회원가입 이메일 인증번호 발송
     * @param email 인증번호를 수신할 이메일 주소
     */
    void sendEmailAuth(String email);

    /**
     * [2단계] 이메일 인증번호 확인
     * @param email   1단계에서 인증번호를 발송한 이메일
     * @param certNum 사용자가 입력한 인증번호
     */
    void verifyNum(String email, String certNum);

    /**
     * [3단계] 회원가입 처리 (Member 저장 + USER_AUTH 권한 부여)
     * @param request  회원가입 요청 정보
     * @param clientIp 회원가입 시점의 클라이언트 IP (last_login_address 저장용)
     */
    void join(AccessionRequest request, String clientIp);
}
