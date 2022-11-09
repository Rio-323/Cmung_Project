package com.sparta.cmung_project.dto;

import com.sparta.cmung_project.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

    @NotBlank(message = "제목을 입력해 주세요")
    @Pattern(regexp = "^.{0,20}$", message = "제목은 20자 이하여야합니다.")
    private String title;

    @NotBlank(message = "내용을 입력해 주세요")
    @Pattern(regexp = "^.{0,150}$", message = "내용은 150자 이하여야합니다.")
    private String content;

    private Category category;

    private int price;

}
