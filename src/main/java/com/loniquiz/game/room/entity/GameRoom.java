package com.loniquiz.game.room.entity;

import com.loniquiz.game.lobby.entity.GameLobby;
import com.loniquiz.users.entity.User;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_game_room")
public class GameRoom {
    @Id
    @GeneratedValue(generator = "uid")
    @GenericGenerator(strategy = "uuid", name = "uid")
    @Column(name = "room_id")
    private String roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "lobby_id")
    private GameLobby gameLobby;

    @Column(name = "room_score")
    private int score; // 게임 스코어
}
