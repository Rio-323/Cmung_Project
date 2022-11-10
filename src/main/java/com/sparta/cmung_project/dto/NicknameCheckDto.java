package com.sparta.cmung_project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class NicknameCheckDto {

    @NotBlank(message = "Nickname을 반드시 입력해야 합니다.")
    @Size(min = 2, max = 20, message = "Nickname의 길이는 2 ~ 20자 입니다.")
    @Pattern( regexp = "^[가-힣ㄱ-ㅎa-zA-Z0-9._-]{2,20}\\$", message = "Nickname은 영문, 한글, 숫자, 특수문자(_)만 입력가능 합니다.")
    private String nickname;
}
