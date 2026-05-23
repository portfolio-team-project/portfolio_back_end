package com.api.domain.base.Member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.base.Member.dto.MemberRequest;
import com.api.domain.base.Member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
	
	private final MemberService memberService;
	
	@PostMapping("/join")
	private String signUp(@RequestBody MemberRequest request) {
		
		
		
		return "OK";
	}	
	
	@GetMapping("/idCheck")
	private boolean idCheck(String userId) {
		
		boolean isDuplicated = memberService.existsByUserId(userId);
		
		
		return isDuplicated;
	}
}
