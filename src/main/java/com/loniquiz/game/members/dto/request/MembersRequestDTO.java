package com.loniquiz.game.members.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembersRequestDTO {
    private String roomId;
}
