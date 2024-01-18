package com.loniquiz.users.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginRequestDTO {
    private String id;
    private String pw;
}
