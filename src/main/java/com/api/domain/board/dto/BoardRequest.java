package com.api.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequest {

    @NotBlank(message = "제목은 필수값입니다.")
    @Size(max = 20, message = "제목은 20자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "내용은 필수값입니다.")
    private String content;
}
