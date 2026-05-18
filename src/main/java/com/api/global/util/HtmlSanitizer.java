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
}
