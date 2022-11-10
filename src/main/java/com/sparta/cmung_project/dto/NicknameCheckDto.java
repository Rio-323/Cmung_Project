package com.sparta.cmung_project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class NicknameCheckDto {
    @NotBlank
    private String nickname;
}
