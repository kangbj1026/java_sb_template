package org.example.java_sb_template.config.exception;

import lombok.extern.slf4j.Slf4j; // 로그 활성화
import org.example.java_sb_template.common.exception.BusinessException; // 커스텀 예외
import org.example.java_sb_template.common.exception.ErrorCode; // 에러 코드
import org.example.java_sb_template.common.response.CommonErrorResponse; // 실패 통합 응답
import org.example.java_sb_template.common.response.ErrorResponse; // 상세 에러 객체
import org.springframework.http.ResponseEntity; // 응답 제어
import org.springframework.web.bind.annotation.ExceptionHandler; // 예외 처리 어노테이션
import org.springframework.web.bind.annotation.RestControllerAdvice; // 전역 처리기

/**
 * 전역 예외 처리기 - 모든 예외를 CommonErrorResponse<ErrorResponse>로 통일하여 반환
 */
@Slf4j
@RestControllerAdvice // 전역 컨트롤러 가로채기 활성화
public class GlobalExceptionHandler {

    /**
     * 비즈니스 로직 처리 중 발생하는 명시적 커스텀 예외 처리
     * ResponseEntity 빌더 방식을 사용하여 타입 추론 안정성 확보 및 IDE 경고 해결
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<CommonErrorResponse<ErrorResponse>> handleBusinessException(BusinessException e) {
        log.error("비즈니스 로직 오류 감지: {}", e.getMessage());
        
        ErrorCode errorCode = e.getErrorCode(); // 예외 내 포함된 에러 규격 추출
        ErrorResponse errorDetails = ErrorResponse.of(errorCode.getErrorCode(), e.getMessage());
        
        // 정적 팩토리 메서드 기반 응답 본문 생성
        CommonErrorResponse<ErrorResponse> responseBody = CommonErrorResponse.ERROR(
                errorCode.getHttpStatus().value(),
                e.getMessage(),
                errorDetails
        );
        
        // 생성자 방식 대신 ResponseEntity 빌더 방식을 사용하여 널 세이프 타입 추론 보장
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(responseBody);
    }

    /**
     * 예기치 못한 시스템 전체 오류 처리
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CommonErrorResponse<ErrorResponse>> handleException(Exception e) {
        log.error("시스템 예외 감지: ", e);
        
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse errorDetails = ErrorResponse.of(errorCode.getErrorCode(), errorCode.getMessage());
        
        CommonErrorResponse<ErrorResponse> responseBody = CommonErrorResponse.ERROR(
                errorCode.getHttpStatus().value(),
                errorCode.getMessage(),
                errorDetails
        );
        
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(responseBody);
    }
}
