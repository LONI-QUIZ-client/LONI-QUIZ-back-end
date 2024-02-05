package com.loniquiz.comment.lobby.entity;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    private String userId;
    private String name;
    private boolean turn;
}
