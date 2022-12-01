package com.sparta.cmung_project.websocket.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.cmung_project.model.Post;
import com.sparta.cmung_project.security.user.UserDetailsImpl;
import com.sparta.cmung_project.websocket.dto.RoomReqDto;
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
    private Long id;

    private String title;

    private String state;

    @JsonManagedReference
    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE ,fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Chat> chats;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;




    private Long postUser;
    private String postNickname;


    private Long joinUser;
    private String joinNickname;



    public Room(Long postUserId,String postNickname, RoomReqDto roomReqDto, UserDetailsImpl userDetails, Post post){

        this.title = roomReqDto.getPostTitle();
        this.postUser = postUserId;
        this.state = post.getState();
        this.postNickname = postNickname;
        this.joinNickname = userDetails.getMember().getNickname();
        this.joinUser = userDetails.getMember().getId();
        this.post = post;

    }

    public void stateUpdate(Post post){
        this.state = post.getState();

    }


}