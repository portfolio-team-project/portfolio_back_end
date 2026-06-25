package com.api.domain.qna.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.qna.dto.QnaDetailResponse;
import com.api.domain.qna.dto.QnaListResponse;
import com.api.domain.qna.dto.QnaMemberRequest;
import com.api.domain.qna.dto.QnaRequest;
import com.api.domain.qna.service.QnaService;
import com.api.global.common.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/qna")
@RequiredArgsConstructor
public class QnaController {
	
	private final QnaService qnaService;
	
	// 게스트 qna 삽입
	@PostMapping("/guest")
	public ResponseEntity<ApiResponse<Void>> insertGuestQna(@Valid @RequestBody QnaRequest qnaRequest,HttpServletRequest request){
		
		qnaService.createGuestQna(qnaRequest, request);
		
		return ResponseEntity.ok(ApiResponse.ok());
	}
	
	// 유저 qna 삽입
	@PostMapping("/member")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse<Void>> insertMemberQna(@Valid @RequestBody QnaMemberRequest qnaMemberRquest,
			                                       @AuthenticationPrincipal String uuid,
			                                       HttpServletRequest request) {
		
		qnaService.createMemberQna(qnaMemberRquest, uuid, request);
		
		return ResponseEntity.ok(ApiResponse.ok());
	}
	
	//목록조회
	@GetMapping("/searchQna")
	public ResponseEntity<ApiResponse<Page<QnaListResponse>>> getQnaList(@RequestParam(required = false) String title,
									                                     @RequestParam(defaultValue = "0") int page) {
		
		Pageable pageable = PageRequest.of(page, 10);
		
		Page<QnaListResponse> qnaList = qnaService.getQnaList(pageable, title,"N","Y");
		
		return ResponseEntity.ok(ApiResponse.ok(qnaList));
	}
	
	//상세조회
	@GetMapping("/detail/{qnaSeq}")
	public ResponseEntity<ApiResponse<QnaDetailResponse>> detailQna(@PathVariable Long qnaSeq){
		
		return ResponseEntity.ok(ApiResponse.ok(qnaService.getQnaDetail(qnaSeq, "N", "Y", true)));
	}

}
