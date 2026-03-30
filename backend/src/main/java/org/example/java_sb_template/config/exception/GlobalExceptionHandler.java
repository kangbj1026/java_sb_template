package org.example.java_sb_template.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.java_sb_template.common.exception.BusinessException;
import org.example.java_sb_template.common.exception.ErrorCode;
import org.example.java_sb_template.common.response.CommonErrorResponse;
import org.example.java_sb_template.common.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j // 로깅 객체 생성 자동화
@RestControllerAdvice // 전역 컨트롤러 가로채기 활성화
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class) // 비즈니스 로직 상의 명시적 예외 처리
    protected ResponseEntity<CommonErrorResponse<ErrorResponse>> handleBusinessException(BusinessException e) {
        log.error("비즈니스 로직 예외 감지: {}", e.getMessage()); // 오류 원인 로깅
        ErrorCode errorCode = e.getErrorCode(); // 예외 내 에러 규격 정보 추출
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getErrorCode(), e.getMessage()); // 상세 에러 객체 생성
        CommonErrorResponse<ErrorResponse> responseBody = CommonErrorResponse.ERROR(errorCode.getHttpStatus().value(), e.getMessage(), errorResponse); // 통합 실패 응답 본문 구성
        return ResponseEntity.status(errorCode.getHttpStatus()).body(responseBody); // HTTP 상태 코드와 함께 최종 반환
    }

    @ExceptionHandler(NoResourceFoundException.class) // 정적 리소스 또는 잘못된 경로 접속 시 처리
    protected ResponseEntity<Void> handleNoResourceFoundException(NoResourceFoundException e) {
        String path = e.getResourcePath(); // 접근을 시도한 경로 추출
        
        // 아이콘, 설정파일 등 일반적인 브라우저 자동 요청 경로가 아닌 경우 보안상 경고 로그 기록
        if (!path.endsWith(".ico") && !path.endsWith(".png") && !path.endsWith(".js") && !path.endsWith(".css")) {
            log.warn("비정상적 경로 접근 시도 감지 (404): {}", path);
        }
        
        return ResponseEntity.notFound().build(); // 404 상태 코드 반환
    }

    @ExceptionHandler(Exception.class) // 정의되지 않은 전역 시스템 예외 처리
    protected ResponseEntity<CommonErrorResponse<ErrorResponse>> handleException(Exception e) {
        log.error("미정의 시스템 예외 감지: ", e); // 상세 스택트레이스를 포함한 오류 로깅
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR; // 기본 서버 오류 코드로 지정
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getErrorCode(), errorCode.getMessage()); // 기본 에러 정보 구성
        CommonErrorResponse<ErrorResponse> responseBody = CommonErrorResponse.ERROR(errorCode.getHttpStatus().value(), errorCode.getMessage(), errorResponse); // 통합 실패 응답 본문 구성
        return ResponseEntity.status(errorCode.getHttpStatus()).body(responseBody); // HTTP 500 상태와 함께 최종 반환
    }
}
