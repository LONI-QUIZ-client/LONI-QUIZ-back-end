package com.loniquiz.auth;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class TokenUserInfo {

    private String userId;
}
