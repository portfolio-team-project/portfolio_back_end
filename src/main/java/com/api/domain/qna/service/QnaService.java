package com.api.domain.qna.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import com.api.domain.qna.dto.QnaDetailResponse;
import com.api.domain.qna.dto.QnaListResponse;
import com.api.domain.qna.dto.QnaMemberRequest;
import com.api.domain.qna.dto.QnaRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface QnaService {
	
	void createGuestQna(QnaRequest qnaRequest,HttpServletRequest request);
	void createMemberQna(QnaMemberRequest qnaMemberRequest, UserDetails user, HttpServletRequest request);
	Page<QnaListResponse> getQnaList(Pageable pageable, String title);
	QnaDetailResponse getQnaDetail(Long qnaSeq);
}
