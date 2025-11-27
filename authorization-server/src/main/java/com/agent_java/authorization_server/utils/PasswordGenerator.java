package com.agent_java.authorization_server.utils;

import java.security.SecureRandom;

public class PasswordGenerator {

    static final char[] CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*".toCharArray();

    static final int CHARS_LENGTH = CHARS.length;

    static final int DEFAULT_PW_LENGTH = 6;

    /**
     * Generates a temporary password for new user accounts.
     *
     * @param length The length of the password to generate (default: 6)
     * @return a random password
     *
     */
    public static String generateTempPassword(int length) {
        // Use SecureRandom for cryptographically secure password generation (OWASP recommendation)
        var random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= length; i++) {
            sb.append(CHARS[random.nextInt(CHARS_LENGTH)]);
        }
        return sb.toString();
    }

    /**
     * Generates a temporary password for new user accounts with default length.
     *
     * @return a random password
     *
     */
    public static String generateTempPassword() {
        return generateTempPassword(DEFAULT_PW_LENGTH);
    }

}
