package com.sparta.cmung_project.websocket.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.model.Post;
import com.sparta.cmung_project.security.user.UserDetailsImpl;
import com.sparta.cmung_project.websocket.dto.RoomReqDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class  Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    private String title;

    private String state;

    @JsonManagedReference
    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE ,fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Chat> chats;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Member postUser;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Member joinUser;


    public Room(Member postUser,Member joinUser, RoomReqDto roomReqDto,Post post){

        this.title = roomReqDto.getPostTitle();
        this.postUser = postUser;
        this.joinUser = joinUser;
        this.state = post.getState();
        this.post = post;

    }

    public void stateUpdate(Post post){
        this.state = post.getState();

    }


}