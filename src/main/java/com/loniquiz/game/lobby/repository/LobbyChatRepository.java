package com.loniquiz.game.lobby.repository;

import com.loniquiz.comment.game.entity.LobbyChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LobbyChatRepository extends JpaRepository<LobbyChat, String> {
}
