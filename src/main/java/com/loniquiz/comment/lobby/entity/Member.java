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
    private String name;
    private boolean state;
}
