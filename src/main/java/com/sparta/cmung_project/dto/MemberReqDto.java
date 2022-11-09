package com.sparta.cmung_project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class MemberReqDto {

    @NotBlank(message = "아이디는 반드시 입력해야합니다")
    @Size(min = 4, max = 12, message = "아이디의 길이는 4 ~ 12입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-z\\d]{4,12}$", message = "아이디는 영문 소문자와 숫자만 입력가능 합니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 반드시 입력해야합니다.")
    @Size(min = 8, max = 20, message = "비밀번호의 길이는 8 ~ 20 입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$", message = "비밀번호는 영문자 대/소문자, 숫자, 특수문자(~!@#$%^&*()+|=)만 입력가능 합니다.")
    private String password;

    @NotBlank
    private String passwordCheck;

    @NotBlank
    private String nickname;


    public MemberReqDto(String userId, String password, String nickname) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
    }

    public void setEncodePwd(String encodePwd) {
        this.password = encodePwd;
    }
}