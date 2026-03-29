package org.example.java_sb_template.common.response;

/**
 * REST API 실패 응답을 전담 처리하는 공통 레코드 (success=false 고정)
 *
 * @param success 처리 성공 유무 식별 플래그 (에러이므로 항상 false)
 * @param message 클라이언트 대상 에러 안내 메시지
 * @param statusCode 행위에 따른 HTTP 상태 코드 (4xx, 5xx)
 * @param error 상세 에러 정보 데이터
 */
public record CommonErrorResponse<T>(
    boolean success, // 실패 고정
    String message, // 클라이언트 대상 에러 메시지
    int statusCode, // HTTP 상태 코드
	ErrorResponse error // 상세 에러 내용
) {
    /**
     * 에러 응답 생성을 위한 정적 팩토리 메서드
     *
     * @param statusCode 에러 상태 코드
     * @param message 에러 설명 메시지
     * @param error 상세 에러 정보
     * @return CommonErrorResponse 객체 반환
     */
    public static <T> CommonErrorResponse<T> ERROR(int statusCode, String message, ErrorResponse error) {
        return new CommonErrorResponse<>(false, message, statusCode, error);
    }
}
