package com.loniquiz.game.lobby.dto.response;


import com.loniquiz.game.members.entity.GameMembers;
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
    private int score;

    public GameRoomUserResponseDTO(GameMembers gameRoom){
        this.userId = gameRoom.getUser().getId();
        this.userNickname = gameRoom.getUser().getNickname();
        this.profile = gameRoom.getUser().getProfileImage();
        this.score = gameRoom.getScore();
    }
}

