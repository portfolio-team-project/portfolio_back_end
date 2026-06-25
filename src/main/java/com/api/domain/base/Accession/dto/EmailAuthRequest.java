package com.api.domain.base.Accession.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원가입 이메일 인증번호 발송(1단계) 요청 DTO
 *
 * sendEmailAuth 엔드포인트에서 수신하며,
 * 유효한 이메일 형식인지 Bean Validation 으로 검증합니다.
 */
@Getter
@NoArgsConstructor
public class EmailAuthRequest {

    /** 인증번호를 수신할 이메일 주소 (필수, 이메일 형식 검증) */
    @NotBlank(message = "이메일은 필수값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
}
