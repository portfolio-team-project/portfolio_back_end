package com.api.domain.base.Accession.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 이메일 인증번호 확인(2단계) 요청 DTO
 *
 * verifyNum 엔드포인트에서 수신하며,
 * 이메일과 인증번호 모두 필수값으로 검증합니다.
 */
@Getter
@NoArgsConstructor
public class VerifyNumRequest {

    /** 1단계에서 인증번호를 발송한 이메일 주소 (필수) */
    @NotBlank(message = "이메일은 필수값입니다.")
    private String email;

    /** 사용자가 입력한 6자리 인증번호 (필수) */
    @NotBlank(message = "인증번호는 필수값입니다.")
    private String certNum;
}
