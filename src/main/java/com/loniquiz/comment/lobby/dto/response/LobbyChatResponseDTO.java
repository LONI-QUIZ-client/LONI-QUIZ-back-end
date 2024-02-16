package com.loniquiz.comment.lobby.dto.response;

import com.loniquiz.comment.lobby.entity.LobbyChat;
import com.loniquiz.users.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LobbyChatResponseDTO {
    private LocalDateTime cmDate;

    private String allCmContent;

    private String nickName;

    public LobbyChatResponseDTO(LobbyChat lobbyChat) {
        this.cmDate = lobbyChat.getCmDate();
        this.allCmContent = lobbyChat.getContent();
        this.nickName = lobbyChat.getUser().getNickname();
    }
}
