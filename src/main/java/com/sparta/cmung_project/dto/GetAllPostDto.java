package com.sparta.cmung_project.dto;

import com.sparta.cmung_project.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAllPostDto {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private String category;
    private String state;
    private String local;
    private int price;
    private String createdAt;


    public static GetAllPostDto getAllPostDto(Post post, String time) {
        return new GetAllPostDto (
                post.getId (),
                post.getMember ().getNickname (),
                post.getTitle (),
                post.getContent (),
                post.getCategory ().getName (),
                post.getState (),
                post.getLocal (),
                post.getPrice (),
                time
        );
    }
}
