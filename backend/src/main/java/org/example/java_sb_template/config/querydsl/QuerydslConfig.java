package org.example.java_sb_template.config.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Querydsl 활용을 위한 시스템 설정 클래스 등록
public class QuerydslConfig {

    @PersistenceContext // 데이터베이스 연동을 위한 JPA 엔티티 매니저 주입
    private EntityManager entityManager;

    @Bean // JPAQueryFactory를 스프링 빈으로 등록하여 서비스 및 레포지토리 레이어에서 타입 안정성이 보장된 동적 쿼리 작성을 지원함
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
