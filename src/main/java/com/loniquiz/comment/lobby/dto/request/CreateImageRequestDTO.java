package com.loniquiz.comment.lobby.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateImageRequestDTO {
    @NotBlank
    private String prompt;
}
