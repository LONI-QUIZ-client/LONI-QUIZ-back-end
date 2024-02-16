package com.loniquiz.follwer.dto.request;

import com.loniquiz.follwer.entity.Follower;
import com.loniquiz.users.entity.User;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollwerRequestDTO {
    private String fid;
    private String userId;

    public Follower toEnity(User user){
        return Follower.builder()
                .follwerId(this.fid)
                .user(user)
                .build();
    }
}
