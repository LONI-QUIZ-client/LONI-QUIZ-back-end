package com.loniquiz.game.lobby.dto.response;

import com.loniquiz.game.lobby.entity.LobbyChat;
import com.loniquiz.users.entity.User;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LobbyChatResponseDTO {
    private String all_cm_content;

    private User user;

    public LobbyChatResponseDTO(LobbyChat lobbyChat) {
        this.all_cm_content = lobbyChat.getContent();
        this.user = lobbyChat.getUser();
    }
}
