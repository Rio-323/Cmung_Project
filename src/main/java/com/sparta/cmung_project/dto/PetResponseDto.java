package com.sparta.cmung_project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetResponseDto {
    private Long id;
    private String name;
    private int age;
    private String category;
    private Long userId;

    public PetResponseDto(Long id, String name, int age, String category) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.category = category;
    }
}
