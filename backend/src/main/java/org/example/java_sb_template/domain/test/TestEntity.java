package org.example.java_sb_template.domain.test;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.java_sb_template.common.domain.BaseEntity;

@Entity // JPA 엔티티 선언
@Getter // Getter 자동 생성
@Table(name = "test_table") // 테이블 명칭 지정
public class TestEntity extends BaseEntity {

    @Id // 기본키 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 값 설정
    private Long id;

    @Column(nullable = false) // 필수 값 설정
    private String content;
}
