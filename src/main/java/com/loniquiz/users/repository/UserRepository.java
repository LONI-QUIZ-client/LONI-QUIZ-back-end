package com.loniquiz.users.repository;

import com.loniquiz.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


// user 레파지토리
public interface UserRepository extends JpaRepository<User, String> {

    // 닉네임으로 회원 찾기
    User findByNickname(String nickname);
    boolean existsById(String id);
    boolean existsByNickname(String nickname);

    @Query(value = "select * from tbl_user order by user_score DESC limit 0, 5", nativeQuery = true)
    List<User> findAllByOrderByScore();

    List<User> findByNicknameContaining(String nickname);

}
