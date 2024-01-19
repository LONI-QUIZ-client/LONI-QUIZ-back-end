package com.loniquiz.game.lobby.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageRequestDTO {
    private int page;
    private int amount;
}
