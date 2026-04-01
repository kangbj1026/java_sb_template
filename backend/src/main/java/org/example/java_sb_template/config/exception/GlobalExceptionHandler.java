package org.example.java_sb_template.config.exception;

import jakarta.servlet.http.HttpServletRequest; // HTTP 요청 정보 확보를 위한 임포트
import lombok.extern.slf4j.Slf4j;
import org.example.java_sb_template.common.exception.BusinessException;
import org.example.java_sb_template.common.exception.ErrorCode;
import org.example.java_sb_template.common.response.CommonErrorResponse;
import org.example.java_sb_template.common.response.ErrorResponse;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice // 전역 예외 처리기 선언
public class GlobalExceptionHandler {

    /**
     * 비즈니스 로직 실행 중 발생하는 커스텀 예외 처리
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<CommonErrorResponse<ErrorResponse>> handleBusinessException(BusinessException e, HttpServletRequest request) {
        // 1. 요청 메서드와 URI를 포함하여 비즈니스 예외 발생 사실 기록
        log.error("[{}] {} - 비즈니스 예외: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        
        // 2. 발생한 예외 객체로부터 미리 정의된 에러 코드 정보 추출
        ErrorCode errorCode = e.getErrorCode();
        
        // 3. 클라이언트 응답 규격에 맞춘 에러 응답 객체 및 본문 구성
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getErrorCode(), e.getMessage());
        CommonErrorResponse<ErrorResponse> responseBody = CommonErrorResponse.ERROR(errorCode.getHttpStatus().value(), e.getMessage(), errorResponse);
        
        // 4. 추출된 HTTP 상태 코드와 함께 최종 응답 객체 반환
        return ResponseEntity.status(errorCode.getHttpStatus()).body(responseBody);
    }

    /**
     * 잘못된 JSON 형식 등 요청 본문을 읽을 수 없을 때 발생하는 예외 처리
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<CommonErrorResponse<ErrorResponse>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        MDC.put("logType", "serverError"); 
        MDC.put("method", "JSON_ERROR");

        log.error("[{}] {} - 잘못된 JSON 요청 본문 감지: {}", request.getMethod(), request.getRequestURI(), e.getMessage());

        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getErrorCode(), "요청 본문의 형식이 올바르지 않음");
        CommonErrorResponse<ErrorResponse> responseBody = CommonErrorResponse.ERROR(errorCode.getHttpStatus().value(), "JSON 파싱 오류 발생", errorResponse);

        MDC.clear();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(responseBody);
    }

    /**
     * @Valid 어노테이션 기반 데이터 검증 실패 시 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<CommonErrorResponse<ErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        MDC.put("logType", "errors");
        MDC.put("method", "ERROR");

        BindingResult bindingResult = e.getBindingResult();
        List<ErrorResponse.FieldError> fieldErrors = bindingResult.getFieldErrors().stream()
                .map(error -> ErrorResponse.FieldError.builder()
                        .field(error.getField())
                        .value(error.getRejectedValue() == null ? "" : error.getRejectedValue().toString())
                        .reason(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        log.error("[{}] {} - 데이터 검증 실패. 필드 오류 개수: {} | 상세: {}", 
                request.getMethod(), request.getRequestURI(), fieldErrors.size(), fieldErrors);

        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(errorCode.getErrorCode())
                .message(errorCode.getMessage())
                .errors(fieldErrors)
                .build();

        CommonErrorResponse<ErrorResponse> responseBody = CommonErrorResponse.ERROR(errorCode.getHttpStatus().value(), "입력값 검증 실패", errorResponse);

        MDC.clear();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(responseBody);
    }

    /**
     * 존재하지 않는 정적 리소스 접근 시 처리
     */
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<Void> handleNoResourceFoundException(NoResourceFoundException e, HttpServletRequest request) {
        String path = e.getResourcePath();
        
        if (!path.endsWith(".ico") && !path.endsWith(".png") && !path.endsWith(".js") && !path.endsWith(".css")) {
            MDC.put("logType", "pathError");
            MDC.put("method", "404");

            String clientIp = getClientIp(request);
            log.warn("[{}] {} - 존재하지 않는 경로 접근 (IP: {})", request.getMethod(), request.getRequestURI(), clientIp);
            
            MDC.clear();
        }
        
        return ResponseEntity.notFound().build();
    }

    /**
     * 기타 정의되지 않은 모든 시스템 예외 처리
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CommonErrorResponse<ErrorResponse>> handleException(Exception e, HttpServletRequest request) {
        MDC.put("logType", "serverError");
        MDC.put("method", "SYSTEM");

        log.error("[{}] {} - 미정의 시스템 예외 발생: ", request.getMethod(), request.getRequestURI(), e);
        
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getErrorCode(), errorCode.getMessage());
        CommonErrorResponse<ErrorResponse> responseBody = CommonErrorResponse.ERROR(errorCode.getHttpStatus().value(), errorCode.getMessage(), errorResponse);
        
        MDC.clear();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(responseBody);
    }

    /**
     * 클라이언트의 실제 IP 주소를 추출함 (Proxy 환경 고려)
     */
    private String getClientIp(HttpServletRequest request) {
        // 1. 프록시 서버나 로드 밸런서를 거친 경우 원래 클라이언트 IP 확인
        String ip = request.getHeader("X-Forwarded-For");
        
        // 2. Apache의 Proxy-Client-IP 헤더 확인
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        
        // 3. WebLogic의 WL-Proxy-Client-IP 헤더 확인
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        
        // 4. 모든 헤더가 없을 경우 요청 객체에서 직접 IP 추출
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        return ip;
    }
}
