package org.example.java_sb_template.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.java_sb_template.common.domain.BaseEntity;
import org.example.java_sb_template.domain.user.constant.UserRole;

@Entity // JPA 엔티티 관리 대상 지정
@Getter // 데이터 조회를 위한 접근자 자동 생성
@Table(name = "users") // 데이터베이스 테이블 명칭 지정 ('user'는 예약어인 경우가 많아 'users' 권장)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 무분별한 객체 생성을 막기 위한 기본 생성자 제한
public class User extends BaseEntity {

    @Id // 기본키 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 데이터베이스 위임형 ID 자동 증가
    private Long id; // 시스템 내부 관리용 고유 식별자

    @Column(nullable = false, unique = true, length = 50) // 필수값, 중복 불가 설정
    private String username; // 사용자 로그인 아이디

    @Column(nullable = false) // 필수값 설정
    private String password; // 암호화된 비밀번호 (평문 저장 금지)

    @Column(nullable = false, length = 100) // 필수값 설정
    private String name; // 사용자 실명 또는 닉네임

    @Enumerated(EnumType.STRING) // Enum 명칭 자체를 문자열로 DB에 저장
    @Column(nullable = false) // 필수값 설정
    private UserRole role; // 사용자에게 부여된 시스템 권한

    @Builder // 안전한 객체 생성을 위한 빌더 패턴 적용
    public User(String username, String password, String name, UserRole role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
    }
}
