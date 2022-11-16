package com.sparta.cmung_project.webSocket.dto;



import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.webSocket.chat.Chat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {
    private String content;
    private Long memberId;
    private String createdAt;

    private  Long roomInfoId;

    private Long chaId;
//    private String proPic;


    public ChatDto (Chat chat) {
        this.chaId=chat.getId();
        Member member = chat.getRoomDetail().getMember();
        this.memberId = member.getId();
//        this.proPic = member.getProPic();
        this.content = chat.getMessage();
        this.roomInfoId = chat.getRoomInfoId();
        this.createdAt = chat.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}