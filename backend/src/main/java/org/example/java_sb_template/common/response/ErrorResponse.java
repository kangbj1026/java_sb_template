package org.example.java_sb_template.common.response;

import lombok.Builder; // 빌더 패턴 적용
import java.util.List; // 리스트 사용

/**
 * 에러 발생 시 상세 정보(코드, 필드 오류 등)를 제공하는 데이터 전달용 레코드
 * 
 * @param errorCode 시스템 내부 정의 에러 식별 코드
 * @param message 예외 상세 안내 메시지
 * @param errors 필드 단위 유효성 검사 실패 목록 (null 허용)
 *   1. ErrorResponse를 record로 변경해야 하는 이유
 *   ErrorResponse처럼 데이터를 단순히 전달하기만 하는 객체(DTO)는 Java 17의 record가 가장 적합
 *    * 불변성(Immutability): record의 모든 필드는 private final로 선언되어 생성 후 값이 변하지 않음을 보장, 에러 응답 정보는 생성 후 수정될 일이 없으므로 보안과 안정성 면에서 뛰어남
 *    * 보일러플레이트 제거: Getter, equals, hashCode, toString 메서드를 컴파일러가 자동으로 생성, 코드가 훨씬 간결해지고 가독성이 좋아짐
 *    * 의도 명확화: "이 객체는 로직이 없는 순수 데이터 전달용이다"라는 의도를 개발자에게 명확하게 전달
 */
@Builder
public record ErrorResponse(
    String errorCode,
    String message,
    List<FieldError> errors
) {
    /**
     * 기본 코드와 메시지만으로 상세 정보를 구성하는 정적 메서드
     */
    public static ErrorResponse of(String errorCode, String message) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .message(message)
                .build();
    }

    /**
     * 필드별 상세 오류 정보를 정의하는 내부 레코드
     */
    @Builder
    public record FieldError(
        String field,
        String value,
        String reason
    ) {}
}
