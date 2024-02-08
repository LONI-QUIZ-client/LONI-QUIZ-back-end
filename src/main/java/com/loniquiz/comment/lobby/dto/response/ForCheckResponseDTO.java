package com.loniquiz.comment.lobby.dto.response;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForCheckResponseDTO {
    private String gno;
    private String answerKey;
}
