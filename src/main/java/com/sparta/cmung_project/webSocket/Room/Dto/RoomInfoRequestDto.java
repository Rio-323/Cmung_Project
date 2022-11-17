package com.sparta.cmung_project.webSocket.Room.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomInfoRequestDto {
    private String nickname;
    private Long me;
    private Long memberId;
    private Long postId;
    private String title;
}
