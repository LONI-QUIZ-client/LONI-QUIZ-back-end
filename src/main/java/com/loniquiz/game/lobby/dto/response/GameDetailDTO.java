package com.loniquiz.game.lobby.dto.response;


import com.loniquiz.game.lobby.entity.GameLobby;
import com.loniquiz.game.room.entity.GameRoom;
import com.loniquiz.users.entity.User;
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
