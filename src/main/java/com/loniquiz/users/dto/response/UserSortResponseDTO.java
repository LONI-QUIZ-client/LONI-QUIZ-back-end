package com.loniquiz.users.dto.response;

import com.loniquiz.users.entity.User;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSortResponseDTO {
    private String id;
    private String nickname;
    private int score;

    public UserSortResponseDTO(User user){
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.score = user.getScore();
    }
}
