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
public class UserResponseDTO {
    private String id;
    private String userNickname;


    public UserResponseDTO(User user){
        this.id = user.getId();
        this.userNickname = user.getNickname();
    }
}
