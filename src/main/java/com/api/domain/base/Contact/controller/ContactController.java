package com.api.domain.base.Contact.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.base.Contact.dto.ContactRequest;
import com.api.global.util.MailUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mail")
public class ContactController {
	
	private final MailUtil mailUtil;
	
	@PostMapping("/contact")
	public ResponseEntity<Void> contact(@Valid @RequestBody ContactRequest request) throws Exception {
		
		
		mailUtil.sendContact(request.getFromName(), request.getFromEmail(), request.getMessage(), request.getRecipient());
		
		return ResponseEntity.ok().build();
	}
}
