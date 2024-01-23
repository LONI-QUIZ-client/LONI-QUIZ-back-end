package com.loniquiz.game.lobby.dto.response;


import com.loniquiz.game.room.entity.GameRoom;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameRoomUserResponseDTO {
    private String userId;
    private String userNickname;
    private String profile;

    public GameRoomUserResponseDTO(GameRoom gameRoom){
        this.userId = gameRoom.getUser().getId();
        this.userNickname = gameRoom.getUser().getNickname();
        this.profile = gameRoom.getUser().getProfileImage();
    }
}

