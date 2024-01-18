package com.loniquiz.users.dto.request;

import com.loniquiz.users.entity.User;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Size;
import java.beans.Encoder;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNewRequestDTO {
    @Size(min = 5, max = 15) // 아이디는 최소 5글자 ~ 최대 15글자
    private String id;
    @Size(min = 8, max = 25) // 비밀번호는 최소 8글자 ~ 최대 25
    private String pw;
    @Size(min = 2, max = 25) // 닉네임은 최소 2글자 ~ 최대 25글자
    private String nickname;
    private String profile; // 이미지

    public User isEntity(PasswordEncoder encoder){
        return User.builder()
                .id(this.id)
                .pw(encoder.encode(this.pw))
                .nickname(this.nickname)
                .profileImage(this.profile)
                .build();
    }
}
