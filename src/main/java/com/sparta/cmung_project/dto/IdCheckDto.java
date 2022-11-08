package com.sparta.cmung_project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class IdCheckDto {

    @NotBlank(message = "아이디는 반드시 입력해야합니다")
    @Size(min = 4, max = 12, message = "아이디의 길이는 4~12입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-z\\d]{4,12}$", message = "아이디는 소문자와 숫자만 포함해야합니다.")
    private String userId;
}
