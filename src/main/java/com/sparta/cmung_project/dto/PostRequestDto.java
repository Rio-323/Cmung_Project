package com.sparta.cmung_project.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sparta.cmung_project.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

    @NotNull(message = "제목을 입력해 주세요.")
    @Size(min = 1, max = 30, message = "제목은 1~30글자로 작성해주세요.")
    private String title;

    @NotNull(message = "내용을 입력해 주세요.")
    @Size(min = 1, max = 200, message = "제목은 1~200글자로 작성해주세요.")
    private String content;

    private String category;

    private String state;

    private int price;
}
