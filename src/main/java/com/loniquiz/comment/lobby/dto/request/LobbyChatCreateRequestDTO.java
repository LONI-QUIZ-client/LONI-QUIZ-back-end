package com.loniquiz.comment.lobby.dto.request;

import com.loniquiz.comment.lobby.entity.LobbyChat;
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
    private String userId;

    public LobbyChat toEntity(User user) {
        return LobbyChat.builder()
                .content(content)
                .user(user)
                .build();
    }
}
