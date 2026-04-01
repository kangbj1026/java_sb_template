package org.example.java_sb_template.common.util;

import org.springframework.util.StringUtils;

public class StringUtil {

    /**
     * 문자열이 비어있는지 확인 (null, "", " " 모두 체크)
     */
    public static boolean isEmpty(String str) {
        return !StringUtils.hasText(str);
    }

    /**
     * 문자열이 특정 길이를 넘어가면 자르고 말줄임표(...)를 붙임
     */
    public static String truncate(String str, int maxLength) {
        if (isEmpty(str) || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }

    /**
     * 핸드폰 번호 등 마스킹 처리 (010-1234-5678 -> 010-****-5678)
     */
    public static String maskPhoneNumber(String phone) {
        if (isEmpty(phone) || !phone.contains("-")) return phone;
        String[] parts = phone.split("-");
        if (parts.length < 3) return phone;
        return parts[0] + "-****-" + parts[2];
    }
}
