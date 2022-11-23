package com.sparta.cmung_project.websocket.dto;


import com.sparta.cmung_project.websocket.domain.Chat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatResDto {

    private String sender;
    private String message;
    private LocalDateTime createdAt;


    public ChatResDto(ChatReqDto chatReqDto, LocalDateTime sendTime){

        this.sender = chatReqDto.getSender();
        this.message = chatReqDto.getMessage();
        this.createdAt = sendTime;
    }
}
