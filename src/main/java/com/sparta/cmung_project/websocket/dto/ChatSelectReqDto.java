package com.sparta.cmung_project.websocket.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatSelectReqDto {
    private Long postId;
    private Long roomId;
}
