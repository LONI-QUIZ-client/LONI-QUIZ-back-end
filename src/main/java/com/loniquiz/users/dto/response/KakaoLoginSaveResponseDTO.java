package com.loniquiz.users.dto.response;

import com.loniquiz.users.entity.User;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoLoginSaveResponseDTO {
    private String id;
    private String nickname;
    private String profileImage;

    public User toEntity(KakaoUserResponseDTO dto, PasswordEncoder encoder){
        return User.builder()
                .id(dto.getId())
                .pw(encoder.encode("0000"))
                .nickname(dto.getProperties().getNickname())
                .createDate(LocalDateTime.now())
                .profileImage(dto.getProperties().getProfileImage())
                .build();
    }
}
