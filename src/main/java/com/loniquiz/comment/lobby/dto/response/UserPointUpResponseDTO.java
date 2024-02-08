package com.loniquiz.comment.lobby.dto.response;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPointUpResponseDTO {
    private String gno;
    private String userId;
}
