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
    @Size(min = 4, max = 60, message = "아이디는 e-mail 형식을 지켜야 합니다.")
    @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "아이디는 e-mail 형식을 지켜야 합니다.")
    private String email;
}
