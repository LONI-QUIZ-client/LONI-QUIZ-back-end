package com.loniquiz.game.room.repository;

import com.loniquiz.game.lobby.entity.GameLobby;
import com.loniquiz.game.room.entity.GameRoom;
import com.loniquiz.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRoomRepository extends JpaRepository<GameRoom, String> {


    //회원과 게임 로비가 존재 하는지 여부 확인
    boolean existsByUserAndGameLobby(User user, GameLobby gameLobby);

    //회원이 방을 나가면 실행할 sql문
    void deleteByUserAndGameLobby(User user, GameLobby gameLobby);
}
