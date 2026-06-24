package com.api.global.util;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.api.domain.base.Member.entity.MemberEntity;
import com.api.global.constants.MailConstant;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MailUtil {
    
	@Value("${email.lee}")
	private String leeEmail;
	
	@Value("${email.ji}")
	private String jiEmail;
	
    private final JavaMailSender javaMailSender;
    
    public void req(String userEmail, String certNum) throws Exception {
        send(userEmail, certNum, "[포트폴리오] 비밀번호 찾기 인증번호", "비밀번호 찾기");
    }

    public void reqSignup(String userEmail, String certNum) throws Exception {
        send(userEmail, certNum, "[포트폴리오] 회원가입 이메일 인증번호", "회원가입 인증");
    }

    private void send(String userEmail, String certNum, String subject, String title) throws Exception {
        ClassPathResource resource = new ClassPathResource("templates/Email_Cert_template.html");
        String template = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        String content = template
                .replace("{{title}}", title)
                .replace("{{certNum}}", certNum);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(userEmail);
        helper.setSubject(subject);
        helper.setText(content, true);

        javaMailSender.send(message);
    }
    
    public void sendContact(String fromName, String fromEmail, String messageBody, String recipient) throws Exception {
    	ClassPathResource resource = new ClassPathResource("templates/Email_Contact_template.html");
    	
    	String receiver = "";
    	String toEmail = "";
    	if ("cb".equals(recipient)) {
    		receiver = MailConstant.LEE;
    		toEmail = leeEmail;
    	}
    	else {
    		receiver = MailConstant.JI;
    		toEmail = jiEmail;
    	}
    	
        String template = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        String content = template
                .replace("{{fromName}}", fromName)
                .replace("{{fromEmail}}", fromEmail)
                .replace("{{messageBody}}",messageBody)
                .replace("{{recipientName}}",receiver);
    	
    	MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(toEmail);
        helper.setSubject("[포트폴리오] " + fromName + "님의 연락");
        helper.setText(content,true);
        javaMailSender.send(message);
    }
    
    public void sendTempPwd(MemberEntity member, String tempPwd) throws Exception {
    	ClassPathResource resource = new ClassPathResource("templates/Email_Chg_Pwd_template.html");
    	
    	String template = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        String content = template
                .replace("{{userName}}", member.getUserName())
                .replace("{{tempPassword}}", tempPwd)
                .replace("{{loginUrl}}", MailConstant.LOGIN_URL);
        
    	MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(member.getEmail());
        helper.setSubject("[포트폴리오] 임시비밀번호");
        helper.setText(content,true);
        javaMailSender.send(message);
    }
}
