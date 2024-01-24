package com.loniquiz.comment.lobby.dto.request;


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
    private int userCount;
    private int maxCount;
    private String userId;


    public GameLobby toEntity(User user){
        return GameLobby.builder()
                .title(this.title)
                .lobbyMaxRound(this.lobbyMaxRound)
                .secret(this.secret)
                .userCount(this.userCount)
                .maxCount(this.maxCount)
                .user(user)
                .build();
    }
}
