package com.api.domain.base.Member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.base.Member.dto.MemberRequest;
import com.api.domain.base.Member.entity.MemberEntity;
import com.api.domain.base.Member.service.MemberService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Validated
public class MemberController {
	
	private final MemberService memberService;
	
	// 회원가입
	@PostMapping("/join")
	public String signUp(@RequestBody MemberRequest request) {
		
		
		
		return "OK";
	}
	
	// 회원탈퇴
	@PostMapping("/withdraw")
	public ResponseEntity<Boolean> withdraw(@RequestBody MemberRequest request){
		
		return ResponseEntity.ok(null);
	}
	
	/*
	 * 비밀번호 찾기
	 * */
	@PostMapping("/findPassword")
	public ResponseEntity<Boolean> findPassword(@RequestParam String userId, @RequestParam String email) {
	    
	    memberService.sendCertificationEmail(userId, email);
	    
	    return ResponseEntity.ok(true);
	}
	
	/*
	 * 인증번호 확인
	 * */
	@PostMapping("/verifyNum")
	public ResponseEntity<Boolean> verifyCertificationNum(@RequestParam String userId, @RequestParam String certificateNum){
	    
	    memberService.verifyCertificationNum(userId, certificateNum);
	    
	    return ResponseEntity.ok(true);
	}
	/*
	 * 비밀번호 재설정
	 * */
	@PostMapping("/rePassword")
	public ResponseEntity<Boolean> rePassword(@RequestParam String userId, @RequestParam String newPassword){
	    
	    memberService.changePassword(userId, newPassword);
	    
	    return ResponseEntity.ok(true);
	}
	
	// ID 중복확인
	@GetMapping("/idCheck")
	public ResponseEntity<Boolean> idCheck(@RequestParam String userId) {
		
		boolean isDuplicated = memberService.existsByUserId(userId);
		
		
		return ResponseEntity.ok(isDuplicated);
	}
	
	// 회원조회
}
