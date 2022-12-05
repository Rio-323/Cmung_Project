package com.sparta.cmung_project.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverMemberInfoDto {

    private String naverId;
    private String nickname;
    private String email;
    private String userImage;
    private String accessToken;
    private String refreshToken;

    @Builder
    public NaverMemberInfoDto(String naverId, String nickname, String email, String userImage, String accessToken, String refreshToken) {
        this.naverId = naverId;
        this.nickname = nickname;
        this.email = email;
        this.userImage = userImage;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
