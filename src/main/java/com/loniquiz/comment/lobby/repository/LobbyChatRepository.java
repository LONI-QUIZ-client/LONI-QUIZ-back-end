package com.loniquiz.comment.lobby.repository;

import com.loniquiz.comment.lobby.entity.LobbyChat;
import com.loniquiz.users.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LobbyChatRepository extends JpaRepository<LobbyChat, Long> {
    List<LobbyChat> findByOrderByCmDateDesc(Pageable pageable);
}
