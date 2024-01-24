package com.loniquiz.users.repository;

import com.loniquiz.users.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class UserRepositoryTest {
    @Autowired
    UserRepository repository;

    @BeforeEach
    void balkInsertBeforEach(){
        User u1 = User.builder()
                .id("jj3713")
                .pw("1234")
                .nickname("정범준")
                .build();
        User u2 = User.builder()
                .id("bb1234")
                .pw("1234")
                .nickname("오영석")
                .build();
        User u3 = User.builder()
                .id("zz123")
                .pw("1234")
                .nickname("이승한")
                .build();

        repository.save(u1);
        repository.save(u2);
        repository.save(u3);
    }


    @Test
    @DisplayName("회원 정보 저장하기")
    void saveTest() {
        //given
        User u1 = User.builder()
                .id("yy123")
                .pw("1234")
                .nickname("김정식")
                .build();
        //when
        User save = repository.save(u1);
        //then
        assertEquals("yy123", save.getId());
    }

    @Test
    @DisplayName("jj3713을 삭제한다")
    void deleteTest() {
        //given
        String id = "jj3713";
        //when
        repository.deleteById(id);
        List<User> userList = repository.findAll();
        //then
        System.out.println("userList = " + userList);
    }

    @Test
    @DisplayName("오영석 닉네임을 정범준으로 바꿀 수 있다")
    void changNicknameTest() {
        //given
        String nickname = "오영석";
        //when
        User byNickname = repository.findByNickname(nickname);
        byNickname.setNickname("정범준");
        User save = repository.save(byNickname);
        //then
        assertEquals("정범준", save.getNickname());
    }



}