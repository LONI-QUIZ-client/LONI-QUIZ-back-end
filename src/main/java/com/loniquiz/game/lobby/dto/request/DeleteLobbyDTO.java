package com.loniquiz.game.lobby.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteLobbyDTO {
    private String gNo;
    private String userId;
}
