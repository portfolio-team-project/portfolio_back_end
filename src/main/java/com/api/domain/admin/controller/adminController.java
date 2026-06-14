package com.api.domain.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	@GetMapping("/member")
	public ResponseEntity<ApiResponse<List<MemberResponse>>> memberDataLoad(){
		
		List<MemberResponse> memberList = memberService.findAllMembers();
		
		return ResponseEntity.ok(ApiResponse.ok(memberList));
	}
	
	@GetMapping("/totalcount")
	public ResponseEntity<Boolean> memberTotalCount() {
		
		
		return ResponseEntity.ok(true);
	}
	
}
