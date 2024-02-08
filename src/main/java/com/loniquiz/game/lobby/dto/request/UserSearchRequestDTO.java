package com.loniquiz.game.lobby.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchRequestDTO {
    private String nickname;
}
