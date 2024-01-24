package com.loniquiz.comment.lobby.dto.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatedImageResponseDTO {
    private List<String> image;
}
