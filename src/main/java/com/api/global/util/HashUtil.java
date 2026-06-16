package com.api.global.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HashUtil {
	
	@Value("${aes.secret-key}") // 32자 키
    private String secretKey;

	public String hash(String value) {
	    try {
	        Mac mac = Mac.getInstance("HmacSHA256");
	        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
	        mac.init(keySpec);
	        return Base64.getEncoder().encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
	    } catch (Exception e) {
	        throw new RuntimeException("해시 실패", e);
	    }
	}

}
