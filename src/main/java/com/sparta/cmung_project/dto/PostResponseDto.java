package com.sparta.cmung_project.dto;

import com.sparta.cmung_project.model.Post;
import com.sparta.cmung_project.util.Chrono;

import java.util.List;

public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private int type;
    private List<String> imgs;

    private String createdAt;


    public PostResponseDto(Post post,List<String> imgs) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
        this.imgs = imgs;
        this.createdAt = Chrono.timesAgo(post.getCreatedAt());
    }
}
