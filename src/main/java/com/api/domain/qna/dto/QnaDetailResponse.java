package com.api.domain.qna.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QnaDetailResponse {
    private Long qnaSeq;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime regDt;
    private String answerYn;
    private String answer;
    private LocalDateTime answerDt;
    private Long viewCnt;
    private boolean isMember;
}

