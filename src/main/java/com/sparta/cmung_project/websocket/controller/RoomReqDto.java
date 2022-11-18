package com.sparta.cmung_project.websocket.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomReqDto {
    private  Long postId;
    private String postTitle;
}
