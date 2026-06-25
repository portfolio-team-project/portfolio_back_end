package com.api.domain.qna.dto;

import java.time.LocalDateTime;

import com.api.domain.qna.entity.QnaEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class QnaListResponse {
    private Long qnaSeq;
    private String nickname;
    private String title;
    private LocalDateTime regDt;
    private String answerYn;
    private Long viewCnt;

    public static QnaListResponse from(QnaEntity qna) {
        return new QnaListResponse(
            qna.getQnaSeq(),
            qna.getNickname(),
            qna.getTitle(),
            qna.getRegDt(),
            qna.getAnswerYn(),
            qna.getViewCnt()
        );
    }
}

