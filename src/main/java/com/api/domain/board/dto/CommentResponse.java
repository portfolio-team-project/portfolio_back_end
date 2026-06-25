package com.api.domain.board.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Long localId;
    private Long boardId;
    private String userId;
    private String content;
    private LocalDate createdDate;
    private LocalDate updatedDate;
}
