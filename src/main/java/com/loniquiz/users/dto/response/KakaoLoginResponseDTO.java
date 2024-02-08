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
public class KakaoLoginResponseDTO {
    private boolean loginSuccess;
    private User user;
}
