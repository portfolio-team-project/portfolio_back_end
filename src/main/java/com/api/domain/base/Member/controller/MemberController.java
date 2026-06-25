package com.api.domain.base.Member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.base.Member.dto.ChangePasswordRequest;
import com.api.domain.base.Member.dto.MemberRequest;
import com.api.domain.base.Member.dto.WithdrawRequest;
import com.api.domain.base.Member.entity.MemberEntity;
import com.api.domain.base.Member.service.MemberService;
import com.api.global.common.ApiResponse;
import com.api.global.constants.MessageConstants;
import com.api.global.exception.BusinessException;
import com.api.global.redis.LoginFailService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Validated
public class MemberController {
	
	private final MemberService memberService;
	private final LoginFailService loginFailService;
	
	// 회원가입
	@PostMapping("/join")
	public String signUp(@RequestBody MemberRequest request) {
		
		
		
		return "OK";
	}
	
	// 회원탈퇴
	@PostMapping("/withdraw")
	public ResponseEntity<ApiResponse<Void>> withdraw(@RequestBody @Valid WithdrawRequest request, HttpServletResponse response) {
		String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		memberService.withdraw(userId, request.getPassword());

		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/api/auth/refresh");
		cookie.setMaxAge(0);
		response.addCookie(cookie);

		return ResponseEntity.ok(ApiResponse.ok(null));
	}
	
	// 소셜 회원탈퇴
	@PostMapping("/socialWithdraw")
	public ResponseEntity<ApiResponse<Void>> socialWithdraw(@AuthenticationPrincipal String uuid,
			                                     HttpServletResponse response) {
		
		memberService.socialWithdraw(uuid);
		
		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/api/auth/refresh");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		
		return ResponseEntity.ok(ApiResponse.ok(null));
	}
	
	/*
	 * 비밀번호 찾기
	 * */
	@PostMapping("/findPassword")
	public ResponseEntity<ApiResponse<String>> findPassword(@RequestParam String userId, @RequestParam String email) {
	    
	    String sessionToken = memberService.sendCertificationEmail(userId, email);
	    
	    return ResponseEntity.ok(ApiResponse.ok(sessionToken));
	}
	
	/*
	 * 인증번호 확인
	 * */
	@PostMapping("/verifyNum")
	public ResponseEntity<ApiResponse<Void>> verifyCertificationNum(@RequestParam String userId, 
			                                                        @RequestParam String certificateNum,
			                                                        @RequestParam String sessionToken){
	    
	    memberService.verifyCertificationNum(userId, certificateNum, sessionToken);
	    
	    return ResponseEntity.ok(ApiResponse.ok());
	}
	/*
	 * 인증메일 비밀번호 재설정
	 * */
	@PostMapping("/resetPassword")
	public ResponseEntity<ApiResponse<Void>> rePassword(@RequestBody ChangePasswordRequest request){
	    
	    memberService.changePassword(request.getUserId(), request.getNewPassword());
	    
	    return ResponseEntity.ok(ApiResponse.ok());
	}
	
	/*
	 * 비밀번호 변경
	 * */
	@PostMapping("/changePassword")
	public ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody ChangePasswordRequest request,
			                                      @AuthenticationPrincipal String uuid,
			                                      HttpServletResponse response) {
		
		String userId = ( uuid != null && !"anonymousUser".equals(uuid) )
		        ? memberService.findByUuid(uuid).getUserId() // 로그인 비밀번호 변경
		        : request.getUserId(); // 3개월 비밀번호 변경
		
		// 1. 잠금 체크 (5회 이상 실패 시 차단)
	    int failCount = loginFailService.getChgPwdFailCount(userId);
	    if (failCount >= 5) {
	        boolean isLoggedIn = uuid != null && !"anonymousUser".equals(uuid);
	        throw new BusinessException(
	            isLoggedIn ? MessageConstants.PWD_CHG_LOCKED          // 로그아웃
	                       : MessageConstants.PWD_CHG_LOCKED_EXPIRED  // 비밀번호 찾기 유도
	        );
	    }
	    
		try {
		    memberService.verifyAndChangePassword(userId, request.getCurrentPassword(), request.getNewPassword());
		    
		    loginFailService.clearChgPwdFailCount(userId);
		    
		} catch (BusinessException e) {
		    // 현재 비번 틀린 경우만 카운트 증가
		    if (MessageConstants.PASSWORD_NOT_MATCH.equals(e.getMessage())) {
		        loginFailService.increaseChgPwdFailCount(userId);
		    }
		    throw e;
		} catch (Exception e) {
		    // 예상치 못한 에러
		    throw new BusinessException(MessageConstants.PWD_CHG_FAILED);
		}
		
	    return ResponseEntity.ok(ApiResponse.ok());
	}
	
	// ID 중복확인
	@GetMapping("/idCheck")
	public ResponseEntity<ApiResponse<String>> idCheck(@RequestParam String userId) {
		return ResponseEntity.ok(ApiResponse.ok(memberService.checkUserId(userId)));
	}
	
	// 회원조회
}
