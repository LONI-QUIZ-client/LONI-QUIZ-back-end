package com.loniquiz.comment.lobby.repository;

import com.loniquiz.comment.lobby.entity.LobbyChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LobbyChatRepository extends JpaRepository<LobbyChat, String> {
}
