package com.api.domain.admin.controller;

import org.springframework.data.domain.Page;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.admin.dto.adminRequest;
import com.api.domain.admin.service.AdminService;
import com.api.domain.base.Member.dto.MemberDetailResponse;
import com.api.domain.base.Member.dto.MemberResponse;
import com.api.domain.base.Member.service.MemberService;
import com.api.global.common.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class adminController {
	
	private final MemberService memberService;
	private final AdminService adminService;
	
	@GetMapping("/member")
	public ResponseEntity<ApiResponse<Page<MemberResponse>>> memberDataLoad(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
		
		Pageable pageable = PageRequest.of(page,size);
		Page<MemberResponse> memberList = memberService.findAllMembers(pageable);
		
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
	
}
