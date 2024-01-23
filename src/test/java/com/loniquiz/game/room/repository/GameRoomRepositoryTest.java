package com.loniquiz.game.room.repository;

import com.loniquiz.game.lobby.entity.GameLobby;
import com.loniquiz.game.lobby.repository.GameLobbyRepository;
import com.loniquiz.game.room.entity.GameRoom;
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
class GameRoomRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    GameLobbyRepository gameLobbyRepository;
    @Autowired
    GameRoomRepository gameRoomRepository;

    @Test
    @DisplayName("게임 방에 사람 넣기")
    void insertUserTest() {
        //given
        User user = userRepository.findById("hh123").orElseThrow();
        GameLobby gameLobby =
                gameLobbyRepository.findById("ff8080818d2fb59f018d2fc0265b0000").orElseThrow();

        GameRoom rm = GameRoom.builder()
                .user(user)
                .gameLobby(gameLobby)
                .build();
        //when
        GameRoom save = gameRoomRepository.save(rm);
        //then
        System.out.println();
        System.out.println("save = " + save);
        System.out.println();
    }


    @Test
    @DisplayName("회원 hh123과 lobby아이디 ff8080818d2fb59f018d2fc0265b0000가 존재하는 확인")
    void existsTest() {
        //given
        User user = userRepository.findById("hh123").orElseThrow();
        GameLobby gameLobby =
                gameLobbyRepository.findById("ff8080818d2fb59f018d2fc0265b0000").orElseThrow();
        //when
        boolean flag =
                gameRoomRepository.existsByUserAndGameLobby(
                        user, gameLobby
                );
        //then
        assertTrue(flag);
    }

    @Test
    @DisplayName("로비 아이디 게임 정보 조회")
    void findByGameRoomTest() {
        //given
        String id = "ff8080818d31390d018d313975240000";
        GameLobby gameLobby = gameLobbyRepository.findById(id).orElseThrow();
        //when
        List<GameRoom> byGameLobby = gameRoomRepository.findByGameLobby(gameLobby);
        //then
        System.out.println("\n \n \n");
        System.out.println("byGameLobby = " + byGameLobby);
        System.out.println("\n \n \n");
    }


}