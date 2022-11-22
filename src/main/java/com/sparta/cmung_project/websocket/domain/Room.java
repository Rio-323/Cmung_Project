package com.sparta.cmung_project.websocket.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.model.Post;
import com.sparta.cmung_project.security.user.UserDetailsImpl;
import com.sparta.cmung_project.websocket.controller.RoomReqDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long postId;

    private String title;


    @JsonManagedReference
    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE ,fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Chat> chats;

    private Long postUser;


    private Long joinUser;



    public Room(Long postUserId, RoomReqDto roomReqDto, UserDetailsImpl userDetails){

        this.postId = roomReqDto.getPostId();
        this.title = roomReqDto.getPostTitle();
        this.postUser = postUserId;
        this.joinUser = userDetails.getMember().getId();
    }

}