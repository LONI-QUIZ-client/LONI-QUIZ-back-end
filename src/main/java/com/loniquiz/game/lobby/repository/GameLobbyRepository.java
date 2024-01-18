package com.loniquiz.game.lobby.repository;

import com.loniquiz.game.lobby.entity.GameLobby;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameLobbyRepository extends JpaRepository<GameLobby, Long> {
}
