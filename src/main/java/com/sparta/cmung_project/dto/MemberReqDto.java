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
    @Size(min = 4, max = 60, message = "아이디는 e-mail 형식을 지켜야 합니다.")
    @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "아이디는 e-mail 형식을 지켜야 합니다.")
    private String email;

    @NotBlank(message = "비밀번호는 반드시 입력해야합니다.")
    @Size(min = 8, max = 20, message = "비밀번호의 길이는 8 ~ 20 입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$", message = "비밀번호는 영문자 대/소문자, 숫자, 특수문자(~!@#$%^&*()+|=)만 입력가능 합니다.")
    private String password;

    @NotBlank
    private String passwordCheck;

    @NotBlank(message = "Nickname을 반드시 입력해야 합니다.")
    @Size(min = 2, max = 20, message = "Nickname의 길이는 2 ~ 20자 입니다.")
    @Pattern ( regexp = "^[가-힣ㄱ-ㅎa-zA-Z0-9._]{2,20}$", message = "Nickname은 영문, 한글, 숫자, 특수문자(_)만 입력가능 합니다.")
    private String nickname;


    public MemberReqDto(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void setEncodePwd(String encodePwd) {
        this.password = encodePwd;
    }
}
