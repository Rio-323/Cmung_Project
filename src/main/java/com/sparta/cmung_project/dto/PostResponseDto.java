package com.sparta.cmung_project.dto;

public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private int type;

    public PostResponseDto(Long id, String title, String content, int type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
    }
}
