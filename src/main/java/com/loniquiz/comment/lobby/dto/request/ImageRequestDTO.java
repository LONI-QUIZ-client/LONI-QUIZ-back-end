package com.loniquiz.comment.lobby.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageRequestDTO {
    private String image;
    private String gno;
}
