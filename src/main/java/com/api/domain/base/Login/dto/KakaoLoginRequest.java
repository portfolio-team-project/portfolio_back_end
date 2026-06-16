package com.api.domain.base.Login.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoLoginRequest {
	
    @NotBlank(message = "아이디는 필수값입니다.")
    private String userId;

    @NotBlank(message = "이름은 필수값입니다.")
    private String userName;

    private String rank;

    private String cpName;

    private String work;

    private String department;

    @NotBlank(message = "카카오 인증 정보가 없습니다.")
    private String kakaoId;

    @NotBlank(message = "이메일은 필수값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @AssertTrue(message = "이용약관에 동의해주세요.")
    private boolean termsAgree;

    @AssertTrue(message = "개인정보 수집·이용에 동의해주세요.")
    private boolean privacyAgree;

    private boolean marketingAgree;
}
