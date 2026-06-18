package com.api.domain.qna.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.domain.base.Member.entity.MemberEntity;
import com.api.domain.base.Member.service.MemberService;
import com.api.domain.qna.dto.QnaDetailResponse;
import com.api.domain.qna.dto.QnaListResponse;
import com.api.domain.qna.dto.QnaMemberRequest;
import com.api.domain.qna.dto.QnaRequest;
import com.api.domain.qna.entity.QnaEntity;
import com.api.domain.qna.repository.QnaRepository;
import com.api.global.constants.MessageConstants;
import com.api.global.exception.BusinessException;
import com.api.global.util.HtmlSanitizer;
import com.api.global.util.HttpUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QnaServiceImpl implements QnaService {

	private final MemberService memberService;
	private final QnaRepository qnaRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	@Transactional
	public void createGuestQna(QnaRequest qnaRequest, HttpServletRequest request) {
		
		String pwd = passwordEncoder.encode(qnaRequest.getQnaPwd());
		String ipAddr = HttpUtil.getClientIp(request);
		String maskNickname = HtmlSanitizer.maskNickname(HtmlSanitizer.sanitize(qnaRequest.getNickname()));
		
		qnaRepository.save(QnaEntity.builder()
				                    .title(HtmlSanitizer.sanitize(qnaRequest.getTitle()))
				                    .nickname(maskNickname)
				                    .content(HtmlSanitizer.sanitize(qnaRequest.getContent()))
				                    .qnaPwd(pwd)
				                    .ipAddr(ipAddr)
				                    .build());
	}

	@Override
	@Transactional
	public void createMemberQna(QnaMemberRequest qnaMemberRequest, UserDetails user, HttpServletRequest request) {
		
		String ipAddr = HttpUtil.getClientIp(request);
		MemberEntity member = memberService.findByUserId(user.getUsername());
		String maskNickname = HtmlSanitizer.maskNickname(member.getUserId());
		
		qnaRepository.save(QnaEntity.builder()
				                    .title(HtmlSanitizer.sanitize(qnaMemberRequest.getTitle()))
				                    .content(HtmlSanitizer.sanitize(qnaMemberRequest.getContent()))
				                    .nickname(maskNickname)
				                    .member(member)
				                    .ipAddr(ipAddr)
				                    .build());
	}

	@Override
	public Page<QnaListResponse> getQnaList(Pageable pageable,String title) {
		Pageable sortedPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), 
                Sort.by(Sort.Direction.DESC, "regDt"));
		
		Page<QnaEntity> result;
		
		if (title == null || title.isBlank()) {
			result = qnaRepository.findByDelYn("N",sortedPage);
		} else {
			result = qnaRepository.findByDelYnAndTitleContaining("N", title, sortedPage);
		}
		
		return result.map(m -> QnaListResponse.builder()
				                              .title(m.getTitle())
				                              .qnaSeq(m.getQnaSeq())
				                              .nickname(m.getNickname())
				                              .regDt(m.getRegDt())
				                              .answerYn(m.getAnswerYn())
				                              .viewCnt(m.getViewCnt())
				                              .build()
				);
	}

	@Override
	@Transactional
	public QnaDetailResponse getQnaDetail(Long qnaSeq) {
		
		
		
		QnaEntity qnaData = qnaRepository.findByQnaSeqAndDelYn(qnaSeq,"N").orElseThrow(
				    ()-> new BusinessException(MessageConstants.SEQ_NOT_FOUND)
				);
		
	    qnaData.increaseViewCnt();
	    
		return QnaDetailResponse.builder()
				                .qnaSeq(qnaData.getQnaSeq())
				                .nickname(qnaData.getNickname())
				                .title(qnaData.getTitle())
				                .content(qnaData.getContent())
				                .regDt(qnaData.getRegDt())
				                .answerYn(qnaData.getAnswerYn())
				                .answer(qnaData.getAnswer())
				                .answerDt(qnaData.getAnswerDt())
				                .viewCnt(qnaData.getViewCnt())
				                .isMember(qnaData.getMember() != null)
				                .build();
	}

}
