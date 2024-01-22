package com.loniquiz.users.repository;

import com.loniquiz.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


// user 레파지토리
public interface UserRepository extends JpaRepository<User, String> {

    // 닉네임으로 회원 찾기
    User findByNickname(String nickname);
}
