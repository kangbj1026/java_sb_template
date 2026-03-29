package org.example.java_sb_template.common.response;

/**
 * REST API 성공 응답을 전담 처리하는 공통 레코드 (success=true 고정)
 * 
 * @param success 처리 성공 유무 식별 플래그
 * @param message 클라이언트 대상 안내 메시지
 * @param statusCode 행위에 따른 HTTP 상태 코드 (200, 201, 204)
 * @param data 실제 결과 데이터 결과물
 */
public record CommonResponse<T>(
    boolean success, // 성공 상태 고정
    String message, // 클라이언트 대상 안내 메시지
    int statusCode, // HTTP 상태 코드
    T data // 실제 결과 데이터
) {
    /** 조회(GET), 수정(PUT/PATCH), 삭제(DELETE) 성공 응답 (200 OK) */
    public static <T> CommonResponse<T> ok(T data) {
        return new CommonResponse<>(true, "Operation successful", 200, data);
    }

    /** 신규 리소스 생성(POST) 성공 응답 (201 Created) */
    public static <T> CommonResponse<T> created(T data) {
        return new CommonResponse<>(true, "Resource created successfully", 201, data);
    }

    /** 응답 본문 없이 성공한 경우 (204 No Content) */
    public static <T> CommonResponse<T> noContent() {
        return new CommonResponse<>(true, "Operation successful with no content", 204, null);
    }
}
