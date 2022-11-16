package com.sparta.cmung_project.webSocket.Room.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomInviteDto {
    private Long memberId;
    private Long postId;
    private String title;
    private String nickname;
}
