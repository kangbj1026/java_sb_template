package org.example.java_sb_template.common.exception; // 공통 예외 패키지 경로 정의

import lombok.Getter; // 에러 코드 조회를 위한 Getter 생성

/**
 * 애플리케이션 비즈니스 로직 수행 중 발생하는 예외를 처리하는 공통 클래스
 * RuntimeException을 상속받아 예외 발생 시 트랜잭션 롤백을 유도함
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode; // 사전 정의된 에러 코드 정보

    /**
     * 기본 에러 코드를 사용하는 생성자
     * 
     * @param errorCode 사전 정의된 에러 코드 객체
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * 기본 에러 메시지 외에 상세 메시지를 추가로 지정하는 생성자
     * 
     * @param message 상세 예외 메시지
     * @param errorCode 사전 정의된 에러 코드 객체
     */
    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
