package com.sparta.cmung_project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class LoginReqDto {

    @NotBlank
    private String userId;

    @NotBlank
    private String password;

    public LoginReqDto(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
