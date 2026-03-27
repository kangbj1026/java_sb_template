package org.example.java_sb_template;

import org.example.java_sb_template.config.property.YamlPropertySourceFactory; // YAML 형식의 프로퍼티 파일을 로드하기 위한 커스텀 팩토리 클래스 임포트
import org.springframework.boot.SpringApplication; // Spring Boot 애플리케이션 구동을 위한 부트스트랩 클래스 임포트
import org.springframework.boot.autoconfigure.SpringBootApplication; // 자동 설정, 컴포넌트 스캔 등을 활성화하는 핵심 어노테이션 임포트
import org.springframework.context.annotation.PropertySource; // 외부 설정 파일 경로를 지정하여 환경 변수에 등록하기 위한 어노테이션 임포트

@SpringBootApplication // Spring Boot 애플리케이션의 구동 환경 및 자동 설정 활성화
@PropertySource(value = "classpath:env.yml", factory = YamlPropertySourceFactory.class) // 외부 설정 파일(env.yml)을 YAML 팩토리를 통해 시스템 환경 변수로 로드
public class JavaSbTemplateApplication { // 시스템의 시작점이 되는 메인 실행 클래스 정의
	
	public static void main(String[] args) { // JVM이 애플리케이션을 실행하는 진입점 메서드
		SpringApplication.run(JavaSbTemplateApplication.class, args); // 설정 정보와 함께 애플리케이션 컨텍스트 가동 및 서버 실행
	}
	
}
