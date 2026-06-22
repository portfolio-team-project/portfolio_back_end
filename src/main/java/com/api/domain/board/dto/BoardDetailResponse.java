package com.api.domain.board.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BoardDetailResponse {
    private Long localId;
    private String userId;
    private String title;
    private String content;
    private LocalDate createdDate;
    private Integer viewCnt;
    private Integer likeCnt;
}
