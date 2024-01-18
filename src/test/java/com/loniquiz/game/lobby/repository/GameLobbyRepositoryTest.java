package com.loniquiz.game.lobby.repository;

import com.loniquiz.game.lobby.entity.GameLobby;
import com.loniquiz.users.entity.User;
import com.loniquiz.users.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class GameLobbyRepositoryTest {
    @Autowired
    GameLobbyRepository gameLobbyRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("회원 한명이 방 생성")
    void makeLobbyTest() {
        //given
        User user = userRepository.findById("jj123").orElseThrow();
        GameLobby gameLobby = GameLobby.builder()
                .title("여기 게임 존잼입니다.!")
                .lobbyMaxRound(3)
                .userCount(0)
                .maxCount(3)
                .user(user)
                .build();
        //when
        GameLobby save = gameLobbyRepository.save(gameLobby);
        //then값
        assertNotNull(save);
    }

    @Test
    @DisplayName("게임 방 전체 조회")
    void findAllRoomTest() {
        //given

        //when
        List<GameLobby> gameRoomList = gameLobbyRepository.findAll();
        //then
        System.out.println("gameRoomList = " + gameRoomList);
    }

    @Test
    @DisplayName("방 삭제")
    void roomRemoveTest() {
        //given
        String id = "ff8080818d1c9d47018d1c9d4d2d0000";
        String userId = "jj123";
        //when
        gameLobbyRepository.deleteWithIdAndUser(id, userId);
        //then
    }



}