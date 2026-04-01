package org.example.java_sb_template.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    // 연산 단위를 식별하기 위한 상수 정의
    public static final int YEAR = 1;
    public static final int MONTH = 2;
    public static final int DAY = 3;

    /**
     * [키워드 기반 포맷팅] 특정 키워드 또는 패턴에 맞춰 날짜를 문자열로 변환함
     * 
     * @param dateTime 변환할 날짜 객체
     * @param pattern 키워드("년", "월", "일", "년,월,일") 또는 일반 패턴
     * @return 포맷팅된 문자열
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) return null;

        // 사용자 정의 키워드 처리 로직
        String finalPattern = switch (pattern) {
            case "년" -> "yyyy년";
            case "월" -> "MM월";
            case "일" -> "dd일";
            case "년,월" -> "yyyy년 MM월";
            case "년,월,일" -> "yyyy년 MM월 dd일";
            default -> pattern; // 그 외에는 입력받은 패턴 그대로 사용
        };

        return dateTime.format(DateTimeFormatter.ofPattern(finalPattern));
    }

    /**
     * 날짜 연산과 표준 포맷팅(yyyy-MM-dd HH:mm:ss)을 한 번에 수행함
     */
    public static String format(LocalDateTime dateTime, int type, int amount) {
        if (dateTime == null) return null;
        
        LocalDateTime result = switch (type) {
            case YEAR -> dateTime.plusYears(amount);
            case MONTH -> dateTime.plusMonths(amount);
            case DAY -> dateTime.plusDays(amount);
            default -> dateTime;
        };
        
        // 연산 결과는 시스템 표준 포맷 문자열로 반환
        return result.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
