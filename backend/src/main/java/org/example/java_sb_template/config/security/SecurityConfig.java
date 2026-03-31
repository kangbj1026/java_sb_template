package org.example.java_sb_template.config.security;

import lombok.RequiredArgsConstructor;
import org.example.java_sb_template.config.security.handler.CustomAuthenticationEntryPoint;
import org.example.java_sb_template.config.security.jwt.JwtAuthenticationFilter;
import org.example.java_sb_template.config.security.jwt.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // 설정 클래스 등록
@EnableWebSecurity // 스프링 시큐리티 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean // 비밀번호 암호화를 위한 인코더 빈 등록
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 안전한 BCrypt 알고리즘 사용
    }

    @Bean // 보안 필터 체인 상세 설정
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // REST API이므로 CSRF 보안 비활성화
            .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 폼 비활성화
            .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 인증 비활성화
            
            // JWT 인증 방식이므로 세션을 사용하지 않도록 설정 (Stateless)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 요청별 권한 제어 설정
            .authorizeHttpRequests(auth -> auth
                // Swagger UI 및 API 문서 관련 경로는 인증 없이 허용
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/api-docs/**", "/favicon.ico").permitAll()
                // 공개 API (예: 로그인, 회원가입) 허용 설정 (필요 시 추가)
                .requestMatchers("/api/auth/**").permitAll()
                // 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
            )
            
            // 인증 실패 시 공통 에러 응답 처리를 위한 엔트리 포인트 등록
            .exceptionHandling(handler -> handler.authenticationEntryPoint(customAuthenticationEntryPoint))
            
            // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 이전에 배치
            .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
