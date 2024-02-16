package com.loniquiz.game.lobby.dto.response;


import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameDetailDTO {
    private List<GameRoomUserResponseDTO> users;
    private GameLobbyDetailDTO gameLobby;
}
