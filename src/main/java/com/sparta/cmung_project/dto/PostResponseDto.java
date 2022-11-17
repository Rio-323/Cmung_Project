package com.sparta.cmung_project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.cmung_project.model.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private int price;
    private String categoryName;
    private String local;
    private String state;
    private List<String> imgs;

    private String createdAt;


    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.price = post.getPrice();
        this.categoryName = post.getCategory().getName();
        this.state = post.getState();
        this.local = post.getLocal();
    }

    public PostResponseDto(Long id, String title, String content, int price, String categoryName,
                           String state, String local, List<String> imageList) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.price = price;
        this.categoryName = categoryName;
        this.imgs = imageList;
        this.state = state;
        this.local = local;
    }


}
