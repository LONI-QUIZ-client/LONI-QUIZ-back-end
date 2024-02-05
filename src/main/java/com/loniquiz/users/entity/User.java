package com.loniquiz.users.entity;
import com.loniquiz.comment.lobby.entity.LobbyChat;
import com.loniquiz.follwer.entity.Follower;
import com.loniquiz.game.members.entity.GameMembers;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter @Getter
@ToString(exclude = {"gameRooms", "lobbyChats", "followers"})
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_user")
public class User {
    // id는 개 개인 특유에 아이디 값만 저장 가능하다.
    @Id
    @Column(name = "user_id")
    private String id; // 사용자 아이디

    @Column(name = "user_pw", nullable = false)
    private String pw; // 사용자 비밀번호

    @Column(name = "user_nickname", nullable = false)
    private String nickname; // 사용자 닉네임

    @CreationTimestamp
    @Column(name = "user_createDate")
    private LocalDateTime createDate; // 사용자가 회원을 가입 한 일

    @Column(name = "user_score", nullable = false) // 기본 값 0
    private int score; // 사용자에 점수 기록을 위한 필드

    @Column(name = "auto_date")
    private LocalDateTime autoDate; // 자동 로그인한 날짜

    @Column(name = "session_id")
    private String sessionId; // 자동 로그인을 위한 세션 아이디

    @Column(name = "profile_image")
    private String profileImage; // 사용자 프로필 이미

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<GameMembers> gameRooms = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<LobbyChat> lobbyChats = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Follower> followers = new ArrayList<>();
}
