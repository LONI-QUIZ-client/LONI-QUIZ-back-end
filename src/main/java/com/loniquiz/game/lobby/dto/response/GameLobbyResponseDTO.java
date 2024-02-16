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
    private String gno;
    private String title;
    private String createDate;
    private int lobbyMaxCount;
    private boolean secret;
    private int userCount;
    private int maxCount;
    private String userNickname;
    private String lobbyCode;

    public GameLobbyResponseDTO(GameLobby gameLobby){
        this.gno = gameLobby.getId();
        this.title = gameLobby.getTitle();
        this.createDate = DateChangeUtil.postDateChang(gameLobby.getCreateDate());
        this.lobbyMaxCount = gameLobby.getLobbyMaxRound();
        this.secret = gameLobby.isSecret();
        this.userCount = gameLobby.getUserCount();
        this.maxCount = gameLobby.getMaxCount();
        this.lobbyCode = gameLobby.getLobby_code();
        this.userNickname = gameLobby.getUser().getNickname();
    }
}
