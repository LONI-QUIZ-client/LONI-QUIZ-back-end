package com.loniquiz.game.lobby.dto.request;

import com.loniquiz.game.lobby.entity.LobbyChat;
import com.loniquiz.users.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LobbyChatCreateRequestDTO {

    @NotBlank
    private String content;

    @NotBlank
    private User user;

    public LobbyChat toEntity() {
        return LobbyChat.builder()
                .content(content)
                .user(user)
                .build();
    }
}
