package com.loniquiz.game.lobby.dto.response;


import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameLobbyDetailDTO {
    private String lobbyId;
    private String createDate;
    private int maxRound;
    private String title;
}
