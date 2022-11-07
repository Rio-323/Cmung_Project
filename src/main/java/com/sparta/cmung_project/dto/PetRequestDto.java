package com.sparta.cmung_project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PetRequestDto {
    private String name;
    private int age;
    private String category;
}
