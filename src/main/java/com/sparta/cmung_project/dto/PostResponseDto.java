package com.sparta.cmung_project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.cmung_project.model.Post;
import com.sparta.cmung_project.time.Time;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private  Long rating;
    private String userImg;

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
        this.rating = post.getMember().getRating();
        this.userImg = post.getMember().getUserImage();
        this.nickname = post.getNickname();
        this.price = post.getPrice();
        this.categoryName = post.getCategory().getName();
        this.state = post.getState();
        this.date = post.getDate();
        this.local = post.getLocal();

        Date date = Timestamp.valueOf(post.getCreatedAt());
        this.createdAt = Time.calculateTime(date);
    }

    public PostResponseDto(Long id, String title, String content,Long rating,String userImg, int price, String categoryName,
                           String state, String local, String date, List<String> imageList,
                           String dateString, String nickname) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.userImg = userImg;
        this.price = price;
        this.categoryName = categoryName;
        this.imgs = imageList;
        this.state = state;
        this.date = date;
        this.local = local;
        this.createdAt = dateString;
        this.nickname = nickname;
    }


}
