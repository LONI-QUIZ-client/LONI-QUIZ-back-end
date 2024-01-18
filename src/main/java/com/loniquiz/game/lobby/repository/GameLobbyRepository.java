package com.loniquiz.game.lobby.repository;

import com.loniquiz.game.lobby.entity.GameLobby;
import com.loniquiz.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameLobbyRepository extends JpaRepository<GameLobby, String> {


    // 방을 삭제할때는 userID와 방에 pk값이 같아야지만 삭제가 가능하다.
    @Query(value = "delete from tbl_game_lobby where lobby_id = :id and user_id = :user_id", nativeQuery = true)
    void deleteWithIdAndUser(@Param("id") String id, @Param("user_id") String user);
}
