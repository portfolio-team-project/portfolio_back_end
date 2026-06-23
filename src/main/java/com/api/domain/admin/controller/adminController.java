package com.api.domain.admin.controller;

import org.springframework.data.domain.Page;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.admin.dto.adminRequest;
import com.api.domain.admin.service.AdminService;
import com.api.domain.base.Member.dto.MemberDetailResponse;
import com.api.domain.base.Member.dto.MemberResponse;
import com.api.domain.base.Member.service.MemberService;
import com.api.domain.qna.dto.QnaDetailResponse;
import com.api.domain.qna.dto.QnaListResponse;
import com.api.domain.qna.service.QnaService;
import com.api.global.common.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class adminController {
	
	private final MemberService memberService;
	private final AdminService adminService;
	private final QnaService qnaService;
	
	@GetMapping("/member")
	public ResponseEntity<ApiResponse<Page<MemberResponse>>> memberDataLoad(@RequestParam(defaultValue = "0") int page,
			                                                                @RequestParam(defaultValue = "10") int size,
			                                                                @RequestParam(defaultValue = "") String keyword,
			                                                                @RequestParam(defaultValue = "userId") String searchType){
		
		Pageable pageable = PageRequest.of(page,size);
		Page<MemberResponse> memberList = memberService.findAllMembers(pageable,keyword,searchType);
		
		return ResponseEntity.ok(ApiResponse.ok(memberList));
	}
	
	@GetMapping("/joinMonthCount")
	public ResponseEntity<ApiResponse<Long>> joinMonthCount() {
		
		Long monthCount = adminService.countThisMonthMembers();
		
		return ResponseEntity.ok(ApiResponse.ok(monthCount));
	}
	
	@GetMapping("/detailUserInfo")
	public ResponseEntity<ApiResponse<MemberDetailResponse>> detailUserInfo(@RequestParam String userId) {
		
		MemberDetailResponse detailUserInfo = adminService.findMemberDetail(userId);
		
		return ResponseEntity.ok(ApiResponse.ok(detailUserInfo));
	}
	
	@GetMapping("/qna")
	public ResponseEntity<ApiResponse<Page<QnaListResponse>>> qnaDataLoad(@RequestParam(defaultValue = "0") int page,
			                                                              @RequestParam(required = false) String delYn,
			                                                              @RequestParam(required = false) String answerYn,
			                                                              @RequestParam(required = false) String title) {
		
		Pageable pageable = PageRequest.of(page, 10);
		Page<QnaListResponse> qnaList = qnaService.getQnaList(pageable, title, delYn, answerYn);
		
		return ResponseEntity.ok(ApiResponse.ok(qnaList));
	}
	
	@GetMapping("/qnaMonthCount")
	public ResponseEntity<ApiResponse<Long>> qnaMonthCount() {
		
		Long monthCount = qnaService.countThisMonthQnaAll("N");
		
		return ResponseEntity.ok(ApiResponse.ok(monthCount));
	}
	
	@GetMapping("/detail/{qnaSeq}")
	public ResponseEntity<ApiResponse<QnaDetailResponse>> detailQna(@PathVariable Long qnaSeq,
															    	@RequestParam(required = false) String delYn,
														            @RequestParam(required = false) String answerYn){
			
		return ResponseEntity.ok(ApiResponse.ok(qnaService.getQnaDetail(qnaSeq, delYn, answerYn, false)));
	}
	
	@PostMapping("/insertQnaAnswer")
	public ResponseEntity<ApiResponse<Void>> insertQnaAnswer(@RequestParam(required = true) Long qnaSeq,
			                                       @RequestParam(required = true) String answer) {
		
		
		qnaService.insertQnaAnswer(qnaSeq, answer);
		
		return ResponseEntity.ok(ApiResponse.ok());
	}
	
	@PostMapping("/deleteQna")
	public ResponseEntity<ApiResponse<Void>> delteQnaData(@RequestParam(required = true) Long qnaSeq){
		
		qnaService.deleteQnaData(qnaSeq);
		
		return ResponseEntity.ok(ApiResponse.ok());
	}
	
	@PostMapping("/deleteId")
	public ResponseEntity<ApiResponse<Void>> deleteIdData(@RequestParam(required = true) String userId) {
		
		adminService.deleteUserId(userId);
		
		return ResponseEntity.ok(ApiResponse.ok());
	}
}
