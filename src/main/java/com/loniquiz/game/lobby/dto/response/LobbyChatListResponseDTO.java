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
public class LobbyChatListResponseDTO {

    private List<LobbyChatResponseDTO> chats;
}
