package org.example.java_sb_template.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

/**
 * 데이터의 생성 일시만 자동으로 기록하는 기반 엔티티 (하이버네이트 기능 활용)
 * 별도의 설정 클래스 없이 어노테이션만으로 동작하며, 수정이 발생하지 않는 테이블에 사용함
 */
@Getter
@MappedSuperclass
public abstract class BaseCreatedEntity {

    @CreationTimestamp // 엔티티 생성 시 현재 시간을 자동으로 기록 (Hibernate 전용)
    @Column(name = "created_at", updatable = false, nullable = false) // 수정 불가 및 필수 입력 설정
    private LocalDateTime createdAt; // 레코드 생성 시점
}
