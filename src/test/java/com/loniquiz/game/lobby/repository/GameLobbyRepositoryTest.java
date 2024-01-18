package com.loniquiz.game.lobby.repository;

import com.loniquiz.game.lobby.entity.GameLobby;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class GameLobbyRepositoryTest {
    @Autowired
    GameLobbyRepository gameLobbyRepository;

    @Test
    @DisplayName("회원 한명이 방 생성")
    void makeLobbyTest() {
        //given
        GameLobby.builder()
                .title("여기 게임 존잼입니다.!")
                .lobbyMaxRound(3)
                .build();
        //when

        //then
    }

}