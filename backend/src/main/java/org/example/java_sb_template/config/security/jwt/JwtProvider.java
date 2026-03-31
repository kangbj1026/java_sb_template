package org.example.java_sb_template.config.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j // 로깅 객체 생성 자동화
@Component // 스프링 빈으로 등록하여 전역에서 주입 가능하게 함
public class JwtProvider {

    private final UserDetailsService userDetailsService;
    private final String secretKeyPlain;
    private final long accessTokenExpiration;
    private SecretKey key; // 서명에 사용될 암호화 키 객체

    public JwtProvider(
            UserDetailsService userDetailsService,
            @Value("${JWT.SECRET}") String secretKeyPlain,
            @Value("${JWT.ACCESS_TOKEN_EXPIRATION}") long accessTokenExpiration
    ) {
        this.userDetailsService = userDetailsService;
        this.secretKeyPlain = secretKeyPlain;
        this.accessTokenExpiration = accessTokenExpiration;
    }

    @PostConstruct // 빈 초기화 직후 실행되어 키 객체 생성
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyPlain); // Base64 문자열 디코딩
        this.key = Keys.hmacShaKeyFor(keyBytes); // HMAC-SHA 알고리즘용 키 생성
    }

    /**
     * 사용자 인증 정보를 기반으로 Access Token 생성
     */
    public String createAccessToken(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .subject(authentication.getName()) // 사용자 식별자 저장 (username)
                .issuedAt(now) // 토큰 발행 시간
                .expiration(expiryDate) // 토큰 만료 시간
                .signWith(key) // 비밀키를 이용한 디지털 서명
                .compact(); // 직렬화하여 문자열로 반환
    }

    /**
     * 토큰에서 사용자 정보를 추출하여 Authentication 객체 생성
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token); // 토큰 복호화 및 정보 추출
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * 토큰 유효성 및 만료 여부 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    /**
     * 토큰 내부의 Payload(Claims) 정보 추출
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
