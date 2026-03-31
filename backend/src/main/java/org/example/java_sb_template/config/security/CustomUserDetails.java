package org.example.java_sb_template.config.security;

import org.example.java_sb_template.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @param user 실제 데이터베이스와 매핑된 회원 엔티티
 */
// 도메인 객체 조회를 위한 접근자 생성
// 파이널 필드 대상 생성자 자동 생성
public record CustomUserDetails(User user) implements UserDetails {
	
	@Override // 사용자에게 부여된 권한 목록 반환 로직 구현
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 엔티티에 정의된 권한 키(ROLE_USER 등)를 Security가 인식할 수 있는 권한 객체로 변환
		return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getKey()));
	}
	
	@Override // 인증에 사용되는 암호화된 비밀번호 반환
	public String getPassword() {
		return user.getPassword();
	}
	
	@Override // 인증에 사용되는 고유 식별 아이디 반환
	public String getUsername() {
		return user.getUsername();
	}
	
	@Override // 계정 만료 여부 확인 (true: 만료되지 않음)
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override // 계정 잠금 여부 확인 (true: 잠기지 않음)
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override // 자격 증명(비밀번호) 만료 여부 확인 (true: 만료되지 않음)
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override // 계정 활성화 여부 확인 (true: 활성화됨)
	public boolean isEnabled() {
		return true;
	}
}
