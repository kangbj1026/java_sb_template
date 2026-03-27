package org.example.java_sb_template.common.response; // 공통 응답 규격 관리를 위한 패키지 경로 정의

import org.example.java_sb_template.common.exception.ErrorCode; // 에러 코드 연동을 위한 임포트

/**
 * REST API 행위별 명확한 의도와 상태 코드를 전달하는 공통 응답 레코드
 */
public record CommonResponse<T>(
    boolean success, // 처리 성공 유무 식별 플래그
    String message, // 클라이언트 대상 안내 메시지
    int statusCode, // HTTP 또는 비즈니스 정의 상태 코드
    T data // 실제 결과 데이터
) {
    /** 조회(GET) 성공 응답 (200 OK) */
    public static <T> CommonResponse<T> ok(T data) {
        return new CommonResponse<>(true, "Operation successful", 200, data);
    }

    /** 생성(POST) 성공 응답 (201 Created) */
    public static <T> CommonResponse<T> created(T data) {
        return new CommonResponse<>(true, "Resource created successfully", 201, data);
    }

    /** 삭제 또는 리턴 데이터가 없는 성공 응답 (204 No Content) */
    public static <T> CommonResponse<T> noContent() {
        return new CommonResponse<>(true, "Operation successful with no content", 204, null);
    }

    /** 수정(PUT/PATCH) 성공 응답 (200 OK) */
    public static <T> CommonResponse<T> updated(T data) {
        return new CommonResponse<>(true, "Resource updated successfully", 200, data);
    }

    /** 삭제(DELETE) 성공 응답 (200 OK) */
    public static <T> CommonResponse<T> deleted() {
        return new CommonResponse<>(true, "Resource deleted successfully", 200, null);
    }

    /**
     * 공통 에러 응답 처리 (ErrorCode 기반)
     * 
     * @param errorCode 사전 정의된 에러 코드 정보 (상태 코드 및 메시지 포함)
     * @return CommonResponse 객체 반환
     */
    public static <T> CommonResponse<T> fail(ErrorCode errorCode) {
        return new CommonResponse<>(false, errorCode.getMessage(), errorCode.getStatus(), null);
    }

    /**
     * 직접 메시지를 지정해야 하는 경우의 에러 응답 처리
     */
    public static <T> CommonResponse<T> error(int statusCode, String message) {
        return new CommonResponse<>(false, message, statusCode, null);
    }
}
