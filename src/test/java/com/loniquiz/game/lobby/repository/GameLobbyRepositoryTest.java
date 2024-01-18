package com.loniquiz.game.lobby.repository;

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
    @DisplayName("회원 한명이 ")
    void makeLobbyTest() {
        //given

        //when

        //then
    }

}