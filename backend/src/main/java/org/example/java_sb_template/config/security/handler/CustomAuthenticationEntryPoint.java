package org.example.java_sb_template.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.java_sb_template.common.exception.ErrorCode;
import org.example.java_sb_template.common.response.CommonErrorResponse;
import org.example.java_sb_template.common.response.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인증되지 않은 사용자가 보호된 리소스에 접근할 때 호출되는 핸들러
 * 표준 CommonErrorResponse 형식의 JSON 응답을 반환함
 */
@Component // 빈 등록
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper; // JSON 변환기

    @Override // 인증 실패 시 실행될 로직 구현
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        
        // 1. 응답 헤더 및 상태 코드 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized

        // 2. 공통 에러 규격에 따른 데이터 구성
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        ErrorResponse errorDetails = ErrorResponse.of(errorCode.getErrorCode(), "Access denied. Please authenticate.");
        CommonErrorResponse<ErrorResponse> errorResponse = CommonErrorResponse.ERROR(
                errorCode.getHttpStatus().value(), 
                errorCode.getMessage(), 
                errorDetails
        );

        // 3. JSON으로 변환하여 스트림에 출력
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
