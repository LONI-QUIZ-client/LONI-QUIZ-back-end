package com.loniquiz.game.lobby.dto.request;


import com.loniquiz.game.lobby.entity.GameLobby;
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
    @NotBlank
    @Size(min = 1, max = 5)
    private int lobbyMaxRound;
    private boolean secret;
    private int userCount;
    @NotBlank
    @Size(min = 1, max = 6)
    private int maxCount;


    public GameLobby toEntity(){
        return GameLobby.builder()
                .title(this.title)
                .lobbyMaxRound(this.lobbyMaxRound)
                .secret(this.secret)
                .userCount(this.userCount)
                .maxCount(this.maxCount)
                .build();
    }
}
