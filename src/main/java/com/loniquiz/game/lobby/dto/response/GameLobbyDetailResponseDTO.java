package com.loniquiz.game.lobby.dto.response;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameLobbyDetailResponseDTO {
    private String title;
    private int maxCount;
    private int userCount;
}
