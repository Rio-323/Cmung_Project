package com.sparta.cmung_project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class MemberReqDto {

    @NotBlank
    private String userId;

    @NotBlank
    private String password;

    public MemberReqDto(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public void setEncodePwd(String encodePwd) {
        this.password = encodePwd;
    }
}
