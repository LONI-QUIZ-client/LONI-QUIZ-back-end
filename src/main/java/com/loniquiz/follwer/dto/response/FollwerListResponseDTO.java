package com.loniquiz.follwer.dto.response;

import com.loniquiz.follwer.entity.Follower;
import com.loniquiz.users.dto.response.UserResponseDTO;
import com.loniquiz.users.entity.User;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollwerListResponseDTO {
    private String fi;
    private String userId;

    public FollwerListResponseDTO(Follower follower) {
        this.fi = follower.getFollwerId();
        this.userId = follower.getUser().getId();
    }
}
