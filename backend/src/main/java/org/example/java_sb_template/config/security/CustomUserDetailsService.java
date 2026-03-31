package org.example.java_sb_template.config.security;

import lombok.RequiredArgsConstructor;
import org.example.java_sb_template.common.exception.BusinessException;
import org.example.java_sb_template.common.exception.ErrorCode;
import org.example.java_sb_template.domain.user.entity.User;
import org.example.java_sb_template.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // 스프링 관리 서비스 빈 등록
@RequiredArgsConstructor // 레포지토리 주입을 위한 생성자 자동 생성
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // 데이터베이스 사용자 조회용 인터페이스

    @Override // 로그인 시 입력된 아이디를 기반으로 사용자 정보를 로드하는 메서드
    @Transactional(readOnly = true) // 읽기 전용 트랜잭션 적용
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DB에서 사용자 조회, 없을 경우 비즈니스 예외 발생
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        // 조회된 엔티티를 Security 인증 규격인 CustomUserDetails로 변환하여 반환
        return new CustomUserDetails(user);
    }
}
