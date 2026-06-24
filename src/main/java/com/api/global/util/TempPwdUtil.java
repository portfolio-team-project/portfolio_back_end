package com.api.global.util;

import java.security.SecureRandom;

public class TempPwdUtil {
	private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGIT = "0123456789";
    private static final String SPECIAL = "!@#$%^&*";
    private static final String ALL = UPPER + LOWER + DIGIT + SPECIAL;

    private static final SecureRandom random = new SecureRandom();

    public static String generate() {
        char[] password = new char[10];

        // 각 종류 최소 1자 보장
        password[0] = UPPER.charAt(random.nextInt(UPPER.length()));
        password[1] = LOWER.charAt(random.nextInt(LOWER.length()));
        password[2] = DIGIT.charAt(random.nextInt(DIGIT.length()));
        password[3] = SPECIAL.charAt(random.nextInt(SPECIAL.length()));

        // 나머지 6자리 랜덤
        for (int i = 4; i < 10; i++) {
            password[i] = ALL.charAt(random.nextInt(ALL.length()));
        }

        // 순서 섞기 (앞 4자리 패턴 노출 방지)
        for (int i = 9; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = password[i];
            password[i] = password[j];
            password[j] = temp;
        }

        return new String(password);
    }
}
