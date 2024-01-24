package com.loniquiz.game.lobby.dto.request;


import com.loniquiz.game.lobby.entity.GameLobby;
import com.loniquiz.users.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameLobbyCreateDTO {

    @NotBlank
    @Size(min = 2, max = 30)
    private String title;
    private int lobbyMaxRound;
    private boolean secret;
    private int maxCount;
    private String userId;


    public GameLobby toEntity(User user, String uuid){
        return GameLobby.builder()
                .title(this.title)
                .lobbyMaxRound(this.lobbyMaxRound)
                .secret(this.secret)
                .maxCount(this.maxCount)
                .lobby_code(uuid)
                .user(user)
                .build();
    }
}
