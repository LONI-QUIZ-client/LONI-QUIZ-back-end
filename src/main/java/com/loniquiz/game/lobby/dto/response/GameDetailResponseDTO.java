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
public class GameDetailResponseDTO {
    int score;
    List<GameLobbyDetailResponseDTO> game;
    List<UserDetailResponseDTO> users;
}
