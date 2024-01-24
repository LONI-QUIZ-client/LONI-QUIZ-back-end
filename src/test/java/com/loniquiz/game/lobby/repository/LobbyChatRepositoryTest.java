package com.loniquiz.game.lobby.repository;

import com.loniquiz.comment.lobby.entity.LobbyChat;
import com.loniquiz.comment.lobby.repository.LobbyChatRepository;
import com.loniquiz.users.entity.User;
import com.loniquiz.users.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class LobbyChatRepositoryTest {
    @Autowired
    LobbyChatRepository lobbyChatRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("채팅을 추가한다")
    void chatCreateTest() {
        // Given
        User u1 = User.builder()
                .id("zz123")
                .pw("1234")
                .nickname("이승한")
                .build();

        userRepository.save(u1);
        String userId = "zz123";
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));

        // When
        String chatContent = "아무튼 채팅임2";
        LobbyChat newChat = LobbyChat.builder()
                .content(chatContent)
                .user(user)
                .build();

        // Then
        LobbyChat savedChat = lobbyChatRepository.save(newChat);
        assertNotNull(savedChat.getId(), "채팅이 저장되었을 때 ID는 null이 아니어야 합니다.");
        assertEquals(chatContent, savedChat.getContent(), "저장된 채팅의 내용이 예상한 내용과 일치해야 합니다.");
        assertEquals(user.getId(), savedChat.getUser().getId(), "저장된 채팅의 사용자 ID가 예상한 사용자 ID와 일치해야 합니다.");
    }
}