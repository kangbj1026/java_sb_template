package org.example.java_sb_template.common.exception;

import lombok.Getter; // 필드에 대한 Getter 자동 생성을 위해 사용
import org.springframework.http.HttpStatus; // Spring 표준 HTTP 상태 코드 라이브러리 임포트

/**
 * 시스템 전역 에러 코드와 대응되는 HTTP 상태 코드를 중앙 집중식으로 관리하는 Enum
 */
@Getter
public enum ErrorCode {
    // 400: 잘못된 요청 관련 에러 코드 정의
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "Invalid input value"),
    TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "C002", "Type mismatch occurred"),
    
    // 401: 인증 실패 관련 에러 코드 정의
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "Authentication required"),
    
    // 403: 권한 부족 관련 에러 코드 정의
    FORBIDDEN(HttpStatus.FORBIDDEN, "A002", "Access is denied"),
    
    // 404: 리소스 미존재 관련 에러 코드 정의
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "Requested resource not found"),
    
    // 409: 리소스 중복/충돌 관련 에러 코드 정의
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "C004", "Resource already exists"),
    
    // 500: 내부 시스템 오류 관련 에러 코드 정의
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "Internal server error occurred"),
    
    // 503: 서비스 가용성 문제 관련 에러 코드 정의
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "S002", "Service is temporarily unavailable");

    private final HttpStatus httpStatus; // 연관된 HTTP 표준 상태 코드 객체
    private final String errorCode; // 비즈니스 로직 식별용 고유 에러 코드
    private final String message; // 클라이언트 대상 에러 안내 메시지

    ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }
}
