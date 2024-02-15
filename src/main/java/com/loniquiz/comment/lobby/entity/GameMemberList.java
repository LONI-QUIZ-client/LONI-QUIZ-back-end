package com.loniquiz.comment.lobby.entity;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameMemberList {

    private String gno;
    private int count;
    private List<Member> members;

}
