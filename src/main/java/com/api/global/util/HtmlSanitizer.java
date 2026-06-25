package com.api.global.util;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class HtmlSanitizer {

    private static final PolicyFactory POLICY =
            Sanitizers.FORMATTING
                    .and(Sanitizers.LINKS)
                    .and(Sanitizers.BLOCKS)
                    .and(Sanitizers.IMAGES);

    public static String sanitize(String html) {
        if (html == null) return null;
        return POLICY.sanitize(html);
    }
    
    public static String maskNickname(String nickname) {
        if (nickname == null || nickname.isEmpty()) return nickname;
        if (nickname.length() == 1) return "*";
        
        return nickname.charAt(0) + "*".repeat(nickname.length() - 1);
    }
}
