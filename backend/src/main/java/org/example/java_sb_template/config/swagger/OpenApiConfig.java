package org.example.java_sb_template.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 스프링 시스템 설정 빈 등록
public class OpenApiConfig {

    @Bean // API 문서의 전역 메타데이터 설정을 위한 빈 정의
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Java Spring Boot API Documentation") // 문서 제목 설정
                        .description("Spring Boot 기반 애플리케이션의 REST API 명세서입니다.") // 문서 상세 설명
                        .version("v0.0.1")); // 애플리케이션 버전 정보 명시
    }

    @Bean // 특정 경로의 API를 그룹화하여 관리하기 위한 빈 정의
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api") // API 그룹 명칭 지정
                .pathsToMatch("/api/**") // Swagger에 노출할 API 경로 패턴 정의
                .build();
    }
}
