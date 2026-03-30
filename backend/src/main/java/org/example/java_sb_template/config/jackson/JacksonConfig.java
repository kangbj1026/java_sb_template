package org.example.java_sb_template.config.jackson;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration // 스프링 시스템 설정 빈 등록
public class JacksonConfig {

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss"; // 표준 날짜 및 시간 포맷 정의

    @Bean // Jackson의 기능을 확장하는 Module을 스프링 빈으로 등록
    public Module javaTimeModule() {
        JavaTimeModule module = new JavaTimeModule(); // Java 8 날짜/시간 API 지원 모듈 생성
        
        // 전역 날짜/시간 포맷터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        
        // LocalDateTime 타입에 대한 직렬화 및 역직렬화 도구 추가
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
        
        return module; // 구성된 모듈 반환 (Spring Boot가 자동으로 ObjectMapper에 등록)
    }
}
