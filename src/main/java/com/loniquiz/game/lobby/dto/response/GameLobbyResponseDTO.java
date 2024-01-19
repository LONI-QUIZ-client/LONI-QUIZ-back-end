package com.loniquiz.game.lobby.dto.response;


import com.loniquiz.game.lobby.entity.GameLobby;
import com.loniquiz.utils.DateChangeUtil;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameLobbyResponseDTO {
    private String title;
    private String createDate;
    private int lobbyMaxCount;
    private boolean secret;
    private int userCount;
    private int maxCount;
    private String userNickname;

    public GameLobbyResponseDTO(GameLobby gameLobby){
        this.title = gameLobby.getTitle();
        this.createDate = DateChangeUtil.postDateChang(gameLobby.getCreateDate());
        this.lobbyMaxCount = gameLobby.getLobbyMaxRound();
        this.secret = gameLobby.isSecret();
        this.userCount = gameLobby.getMaxCount();
        this.userNickname = gameLobby.getUser().getNickname();
    }
}
