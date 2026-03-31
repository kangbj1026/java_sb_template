package org.example.java_sb_template.config.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * HTTP 요청마다 한 번씩 실행되어 JWT 토큰의 유효성을 검사하는 보안 필터
 */
@RequiredArgsConstructor // 생성자 주입을 위한 어노테이션
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider; // JWT 생성 및 검증 컴포넌트

    @Override // 필터링 핵심 로직 구현
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 1. 요청 헤더에서 JWT 토큰 추출
        String token = resolveToken(request);

        // 2. 토큰 유효성 검사 후 성공 시 인증 정보를 컨텍스트에 저장
        if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication); // 시스템 전역 인증 객체 등록
        }

        // 3. 다음 필터 체인으로 요청 위임
        filterChain.doFilter(request, response);
    }

    /**
     * 헤더의 'Authorization' 항목에서 'Bearer ' 접두사를 제거한 실제 토큰값 추출
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후의 값만 반환
        }
        return null;
    }
}
