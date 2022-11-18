package com.sparta.cmung_project.websocket.domain;

import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.model.Post;
import com.sparta.cmung_project.security.user.UserDetailsImpl;
import com.sparta.cmung_project.websocket.controller.RoomReqDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long postId;

    private String title;

    private Long postUser;


    private Long joinUser;



    public Room(Post post, RoomReqDto roomReqDto, UserDetailsImpl userDetails){

        this.postId = post.getId();
        this.title = roomReqDto.getPostTitle();
        this.postUser = post.getMember().getId();
        this.joinUser = userDetails.getMember().getId();
    }

}