package com.loniquiz.game.lobby.entity;

import com.loniquiz.users.entity.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_game_room")
public class GameLobby {
    @Id
    @GeneratedValue(generator = "uid")
    @GenericGenerator(strategy = "uuid", name = "uid")
    @Column(name = "lobby_id")
    private String Id; // pk를 위한 유일한 값

    @Column(name = "lobby_title", nullable = false)
    private String title; // 사용자가 정한 게임 제목?

    @CreationTimestamp
    @Column(name = "lobby_create_date")
    private LocalDateTime createDate; // 방 생성 날짜

    @Column(name = "lobby_max_round", nullable = false)
    private int lobbyMaxRound; // 사용자가 정한 최대 라운드

    @Column(name = "lobby_secret")
    private boolean secret; // 비밀방 or 공개방

    @Column(name = "lobby_code")
    private String lobby_code; // 비번방시 필요한 code값

    @Column(name = "lobby_user_count", nullable = false)
    private int userCount; // 로비에 들어온 회원 수

    @Column(name = "lobby_max_count", nullable = false)
    private int maxCount; // 로비 최대 인원 설정

    @OneToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
