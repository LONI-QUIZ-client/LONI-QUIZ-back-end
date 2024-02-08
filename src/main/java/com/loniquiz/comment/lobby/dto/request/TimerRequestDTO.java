package com.loniquiz.comment.lobby.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimerRequestDTO {
    private String gno;
    private int time;
}
