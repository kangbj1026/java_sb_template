package org.example.java_sb_template.config.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing 기능을 활성화하여 엔티티의 생성/수정 시간을 자동 기록함
 */
@Configuration // 스프링 시스템 설정 빈으로 등록
@EnableJpaAuditing // JPA Auditing 기능 활성화 (BaseEntity 동작을 위해 필수)
public class JpaConfig {
}
