package com.sparta.cmung_project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.cmung_project.model.Post;
import com.sparta.cmung_project.time.Time;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;

    private String nickname;
    private int price;
    private String categoryName;
    private String state;
    private String local;
    private String date;
    private List<String> imgs;

    private String createdAt;


    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.nickname = post.getNickname();
        this.price = post.getPrice();
        this.categoryName = post.getCategory().getName();
        this.state = post.getState();
        this.date = post.getDate();
        this.local = post.getLocal();

        Date date = Timestamp.valueOf(post.getCreatedAt());
        this.createdAt = Time.calculateTime(date);
    }

    public PostResponseDto(Long id, String title, String content, int price, String categoryName,
                           String state, String local, String date, List<String> imageList) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.price = price;
        this.categoryName = categoryName;
        this.imgs = imageList;
        this.state = state;
        this.date = date;
        this.local = local;
    }


}
