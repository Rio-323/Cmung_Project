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



    public Room(Long id, RoomReqDto roomReqDto, UserDetailsImpl userDetails){

        this.postId = roomReqDto.getPostId();
        this.title = roomReqDto.getPostTitle();
        this.postUser = id;
        this.joinUser = userDetails.getMember().getId();
    }

}