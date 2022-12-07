package com.sparta.cmung_project.websocket.dto;


import com.sparta.cmung_project.dto.MemberResponseDto;
import com.sparta.cmung_project.dto.PostResponseDto;
import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.model.Post;
import com.sparta.cmung_project.websocket.domain.Chat;
import com.sparta.cmung_project.websocket.domain.Room;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RoomResponseDto {


    private Long postId;

    private String postImg;
    private Long roomId;

    private String title;
    private int price;

    private String state;
    private List<Chat> chatList;

    private LocalDateTime lastCHatTime;
    private Long postUserId;
    private String postUserImg;
    private String postUserNickname;

    private Long joinUserId;
    private String joinUserImg;
    private String joinUserNickname;




    public RoomResponseDto(Room room){

        this.postId = room.getPost().getId();
        this.roomId = room.getId();
        this.title = room.getTitle();
        this.postImg = room.getPost().getImage().get(0).getImage();
        this.price = room.getPost().getPrice();
        this.chatList = room.getChats();
        this.state = room.getState();
//        this.lastCHatTime = room.getChats().get(chatList.size()).getSendDate(); 삼항연산자... 성우님
        this.postUserId = room.getPostUser().getId();
        this.postUserImg = room.getPostUser().getUserImage();
        this.postUserNickname = room.getPostUser().getNickname();

        this.joinUserId = room.getJoinUser().getId();
        this.joinUserImg = room.getJoinUser().getUserImage();
        this.joinUserNickname = room.getJoinUser().getNickname();


    }

}
