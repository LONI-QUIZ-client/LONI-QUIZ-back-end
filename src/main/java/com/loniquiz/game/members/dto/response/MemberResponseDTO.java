package com.loniquiz.game.members.dto.response;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponseDTO {
    private String userId;
    private String gno;
}
