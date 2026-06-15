package com.api.domain.base.Contact.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ContactRequest {
	
    @NotBlank(message = "이름을 입력해주세요.")
    @Pattern(regexp = "^[가-힣\\s]{1,50}$", message = "이름은 한글만 입력 가능합니다.")
	private String fromName;
	
    @NotBlank(message = "이메일을 입력해주세요.")
	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "이메일 형식이 올바르지 않습니다.")
    private String fromEmail;
    
    @NotBlank(message = "메시지를 입력해주세요.")
    private String message;
    
    @Pattern(regexp = "^(ca|cb)$", message = "올바르지 않은 수신자입니다.")
    private String recipient;  // 두명만 쓰기에 추가 추후 수정 필요 시 수정
}
