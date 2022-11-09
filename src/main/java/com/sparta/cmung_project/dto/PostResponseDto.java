package com.sparta.cmung_project.dto;

import com.sparta.cmung_project.model.Post;
import com.sparta.cmung_project.util.Chrono;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PostResponseDto {
    private Long id;
    private String title;
    private String content;

    private String category;

    private String state;

    private List<String> imgs;
    private String createdAt;


    public PostResponseDto(Post post) {
        this.id = post.getId();

    }
}
