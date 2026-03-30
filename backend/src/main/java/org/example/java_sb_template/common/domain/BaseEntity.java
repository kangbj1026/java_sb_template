package org.example.java_sb_template.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

/**
 * 생성 일시(BaseCreatedEntity 상속)와 수정 일시를 모두 관리하는 기반 엔티티 (하이버네이트 기능 활용)
 * 별도의 설정 없이 상태 변화가 발생하는 일반적인 비즈니스 테이블에 상속하여 사용함
 */
@Getter
@MappedSuperclass  // 상속받는 엔티티에 매핑 정보만 전달 ( 이 클래스는 직접 테이블로 생성되지 않음 )
public abstract class BaseEntity extends BaseCreatedEntity {

    @UpdateTimestamp // 엔티티 수정 시 현재 시간을 자동으로 갱신 (Hibernate 전용)
    @Column(name = "updated_at", nullable = false) // 필수 입력 설정
    private LocalDateTime updatedAt; // 레코드 최종 수정 시점
}
