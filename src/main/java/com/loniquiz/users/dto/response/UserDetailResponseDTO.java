package com.loniquiz.users.dto.response;

import com.loniquiz.users.entity.User;
import com.loniquiz.utils.DateChangeUtil;
import lombok.*;

@Setter @Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailResponseDTO {

    private String id; // 회원 아이디
    private String pw; // 회원 비밀번호
    private String nickname; // 회원 닉네임
    private String createDate; // 회원 가입 일자
    private int score; // 회원 점수
    
    public UserDetailResponseDTO(User user){
        this.id = user.getId();
        this.pw = user.getPw();
        this.nickname = user.getNickname();
        this.createDate = DateChangeUtil.postDateChang(user.getCreateDate());
        this.score = user.getScore();
    }
}
