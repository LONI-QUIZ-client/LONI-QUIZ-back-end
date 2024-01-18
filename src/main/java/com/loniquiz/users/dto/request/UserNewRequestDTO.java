package com.loniquiz.users.dto.request;

import com.loniquiz.users.entity.User;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNewRequestDTO {
    private String id;
    private String pw;
    private String nickname;
    private String profile; // 이미지

    public User isEntity(){
        return User.builder()
                .id(this.id)
                .pw(this.pw)
                .nickname(this.nickname)
                .profileImage(this.profile)
                .build();
    }
}
