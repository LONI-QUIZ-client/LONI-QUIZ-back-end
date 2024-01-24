package com.loniquiz.comment.game.entity;

import com.loniquiz.users.entity.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "tbl_all_comment")
public class LobbyChat {

    @Id
    @Column(name = "all_cm_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "all_cm_content")
    private String content; // 사용자가 입력한 채팅 내용

    @CreationTimestamp
    @Column(name = "all_cm_date")
    private LocalDateTime cmDate; // 채팅 친 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


}
