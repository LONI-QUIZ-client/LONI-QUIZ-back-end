package com.loniquiz.game.lobby.entity;

import com.loniquiz.users.entity.User;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_game_lobby")
public class GameLobby {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lobby_id")
    private Long Id; // pk를 위한 유일한 값

    @Column(name = "lobby_title", nullable = false)
    private String title; // 사용자가 정한 게임 제목?

    @Column(name = "lobby_max_round", nullable = false)
    private String lobbyMaxRound; // 사용자가 정한 최대 라운드

    @Column(name = "lobby_secret")
    private boolean secret; // 비밀방 or 공개방

    @Column(name = "lobby_code")
    private String lobby_code; // 비번방시 필요한 code값

    @Column(name = "lobby_user_count", nullable = false)
    private int userCount; // 방에 들어온 회원 수

    @Column(name = "lobby_max_count", nullable = false)
    private int maxCount;

    @OneToOne
    @Column(name = "user_id")
    private User user;
}
