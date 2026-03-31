package org.example.java_sb_template.domain.user.repository;

import org.example.java_sb_template.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 로그인 아이디를 기반으로 사용자 정보를 조회함
     * 
     * @param username 로그인 아이디
     * @return Optional 객체로 감싸진 사용자 엔티티
     */
    Optional<User> findByUsername(String username);

    /**
     * 중복 가입 여부 확인을 위한 아이디 존재 여부 체크
     * 
     * @param username 확인하려는 아이디
     * @return 존재 여부 (true/false)
     */
    boolean existsByUsername(String username);
}
