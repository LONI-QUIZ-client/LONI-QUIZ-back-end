package com.loniquiz.game.lobby.repository;

import com.loniquiz.game.lobby.entity.LobbyChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LobbyChatRepository extends JpaRepository<LobbyChat, String> {
}
