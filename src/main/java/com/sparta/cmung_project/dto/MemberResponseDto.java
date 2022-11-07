package com.sparta.cmung_project.dto;

public class MemberResponseDto {
    private Long id;
    private String nickname;
    private String userImage;

    public MemberResponseDto(Long id, String nickname, String userImage) {
        this.id = id;
        this.nickname = nickname;
        this.userImage = userImage;
    }
}
