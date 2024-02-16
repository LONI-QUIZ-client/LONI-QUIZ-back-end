package com.loniquiz.game.lobby.repository;

import com.loniquiz.game.lobby.entity.GameLobby;
import com.loniquiz.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface GameLobbyRepository extends JpaRepository<GameLobby, String> {

    // 게임방 페이징 처리
    Page<GameLobby> findAll(Pageable pageable);

    boolean existsByUser(User user);

    // 방을 삭제할때는 user_id와 방에 lobby_id 값이 같아야지만 삭제가 가능하다.
    @Query(value = "delete from tbl_game_room where lobby_id = :lobbyId and user_id = :userId", nativeQuery = true)
    void deleteWithIdAndUser(@Param("lobbyId") String id, @Param("userId") String user);

    //방 들어올때 회원 수 한명 증가
    @Query(value = "update tbl_game_room set lobby_user_count = lobby_user_count + 1 where lobby_id = :lid",
            nativeQuery = true)
    void upUserCount(String lid);

    // 방 나갈때 회원 수 한명 나가리
    @Query(value = "update tbl_game_room set lobby_user_count = lobby_user_count - 1 where lobby_id = :lid",
            nativeQuery = true)
    void downUserCount(String lid);

}
