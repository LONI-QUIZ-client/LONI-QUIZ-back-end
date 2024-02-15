package com.loniquiz.comment.lobby.dto.response;

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
    private String username;
    private String profileImage;
    private int maxUser;
    private int lobbyMaxCount;
}
