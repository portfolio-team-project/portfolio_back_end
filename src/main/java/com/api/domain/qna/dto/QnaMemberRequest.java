package com.api.domain.qna.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QnaMemberRequest {
	
	@NotBlank(message = "제목은 필수값입니다.")
	private String title;
	
	@NotBlank(message = "내용은 필수값입니다.")
	private String content;
	
}
