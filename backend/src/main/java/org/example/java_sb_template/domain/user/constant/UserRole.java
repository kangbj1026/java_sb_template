package org.example.java_sb_template.domain.user.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter // 필드 접근자 생성
@RequiredArgsConstructor // 파이널 필드 생성자 자동 생성
public enum UserRole {

    ADMIN("ROLE_ADMIN", "시스템 관리자"), // 시스템 전역 관리 권한
    USER("ROLE_USER", "일반 사용자"); // 일반적인 비즈니스 서비스 이용 권한

    private final String key; // Spring Security에서 인식하는 권한 식별자 (ROLE_ 접두사 필수)
    private final String description; // 권한에 대한 한글 설명
}
