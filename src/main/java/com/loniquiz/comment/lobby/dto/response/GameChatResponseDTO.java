package com.loniquiz.comment.lobby.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameChatResponseDTO {
    private String gno;
    private String userId;
    private String content;
    private LocalDateTime localDateTime;
}
