package com.api.domain.base.Accession.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원가입(3단계) 요청 DTO
 *
 * Frontend의 AccessionItem 타입과 필드를 맞춥니다.
 * work, department 는 현재 Member 테이블에 컬럼이 없으므로 수신만 하고 저장하지 않습니다.
 *
 * [Backend 유효성 검사 이유]
 *   Frontend 검사는 브라우저 우회(Postman, curl 등)가 가능합니다.
 *   Backend 검사는 API 레이어의 마지막 방어선으로, 악의적 요청을 차단합니다.
 */
@Getter
@NoArgsConstructor
public class AccessionRequest {

    /** 회원 아이디 (필수, 공백 불가) */
    @NotBlank(message = "아이디는 필수값입니다.")
    private String userId;

    /** 회원 이름 (필수, 공백 불가) */
    @NotBlank(message = "이름은 필수값입니다.")
    private String userName;

    /** 직급 (선택) */
    private String rank;

    /** 회사명 (선택) */
    private String cpName;

    /** 업무 (선택 — Member 테이블에 컬럼 없음, 추후 확장용) */
    private String work;

    /** 부서 (선택 — Member 테이블에 컬럼 없음, 추후 확장용) */
    private String department;

    /**
     * 비밀번호 (필수)
     * 8자 이상, 영문 + 숫자 + 특수문자(!@#$%^&*) 각 1자 이상 포함
     */
    @NotBlank(message = "비밀번호는 필수값입니다.")
    @Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$",
        message = "비밀번호는 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다."
    )
    private String password;

    /** 이메일 (필수, 이메일 형식 검증) */
    @NotBlank(message = "이메일은 필수값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    /** 이용약관 동의 (필수 — false 이면 유효성 검사 실패) */
    @AssertTrue(message = "이용약관에 동의해주세요.")
    private boolean termsAgree;

    /** 개인정보 수집·이용 동의 (필수 — false 이면 유효성 검사 실패) */
    @AssertTrue(message = "개인정보 수집·이용에 동의해주세요.")
    private boolean privacyAgree;

    /** 마케팅 정보 수신 동의 (선택) */
    private boolean marketingAgree;
}
