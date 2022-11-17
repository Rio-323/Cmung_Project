package com.sparta.cmung_project.dto;

import com.sparta.cmung_project.model.Post;
import com.sparta.cmung_project.time.Time;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AllPostResponseDto {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private String category;
    private String state;
    private String local;
    private String date;
    private int price;
    private List<String> imgs;
    private String createdAt;

    public AllPostResponseDto(Post post, List<String> imgs) {
        this.id = post.getId();
        this.nickname = post.getMember().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory().getName();
        this.state = post.getState();
        this.local = post.getLocal();
        this.date = post.getDate();
        this.price = post.getPrice();
        this.imgs = imgs;

        // 시간 처리
        Date date = Timestamp.valueOf(post.getCreatedAt());
        this.createdAt = Time.calculateTime(date);
    }
}
