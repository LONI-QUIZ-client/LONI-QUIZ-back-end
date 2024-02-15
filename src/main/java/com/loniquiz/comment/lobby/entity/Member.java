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
    private String profileImage;
    private boolean turn;
    @Builder.Default
    private int point = 0;
}
