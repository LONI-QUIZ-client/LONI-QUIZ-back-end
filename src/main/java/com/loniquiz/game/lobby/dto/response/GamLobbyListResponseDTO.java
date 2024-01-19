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
public class GamLobbyListResponseDTO {
    private int pageMaxCount; // 페이지에 마지막
    private List<GameLobbyResponseDTO> dto;
}
