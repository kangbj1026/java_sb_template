package org.example.java_sb_template.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Slf4j // 로깅 활성화
public class SecurityUtil {

    /**
     * 현재 스레드의 Security Context에서 인증된 사용자의 로그인 아이디를 추출함
     * 
     * @return 로그인 아이디(username)를 담은 Optional 객체
     */
    public static Optional<String> getCurrentUsername() {
        // 1. 보안 컨텍스트에서 인증 정보 추출
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.debug("Security Context에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        // 2. 인증 객체 타입에 따른 사용자 아이디 추출
        String username = null;
        if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            username = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String principalStr) {
            username = principalStr;
        }

        return Optional.ofNullable(username);
    }
}
