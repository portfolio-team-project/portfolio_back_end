package com.api.global.util;

import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MailUtil {
    
    private final JavaMailSender javaMailSender;
    
    public void req(String userEmail, String certNum) throws Exception {
        
        ClassPathResource resource = new ClassPathResource("templates/Email_Cert_template.html");
        String template = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        String content = template.replace("{{certNum}}", certNum);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(userEmail);
        helper.setSubject("[포트폴리오] 비밀번호 찾기 인증번호");
        helper.setText(content, true); // true = HTML 사용
        
        javaMailSender.send(message);
        
    }
}
