package com.loniquiz.users.dto.response;


import com.loniquiz.game.lobby.dto.request.UserSearchRequestDTO;
import com.loniquiz.users.entity.User;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchResponseDTO {
    private String id;
    private String nickname;

    public UserSearchResponseDTO(User user){
        this.id = user.getId();
        this.nickname = user.getNickname();
    }
}
