package org.example.java_sb_template.common.exception; // 공통 예외 규격 관리를 위한 패키지 정의

import lombok.Getter; // 필드에 대한 Getter 자동 생성을 위해 사용

/**
 * 시스템 전체에서 사용할 에러 코드와 메시지를 중앙 집중식으로 관리하는 Enum
 */
@Getter
public enum ErrorCode {
    // 400: 유효성 검사 실패 또는 잘못된 요청
    INVALID_INPUT_VALUE(400, "Invalid input value"),
    TYPE_MISMATCH(400, "Type mismatch occurred"),
    
    // 401: 인증 실패
    UNAUTHORIZED(401, "Authentication required"),
    
    // 403: 권한 없음
    FORBIDDEN(403, "Access is denied"),
    
    // 404: 리소스 없음
    RESOURCE_NOT_FOUND(404, "Requested resource not found"),
    
    // 409: 데이터 중복/충돌
    DUPLICATE_RESOURCE(409, "Resource already exists"),
    
    // 500: 내부 서버 오류
    INTERNAL_SERVER_ERROR(500, "Internal server error occurred"),
    
    // 503: 서비스 점검/과부하
    SERVICE_UNAVAILABLE(503, "Service is temporarily unavailable");

    private final int status; // HTTP 상태 코드 또는 비즈니스 식별 코드
    private final String message; // 에러에 대한 상세 설명 문구

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
