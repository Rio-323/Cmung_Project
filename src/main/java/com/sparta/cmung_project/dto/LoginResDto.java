package com.sparta.cmung_project.dto;

import com.sparta.cmung_project.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResDto {

    private String email;
    private String nickname;
    private String userImage;

    public LoginResDto(Member member, String userImage) {
        this.email = member.getEmail ();
        this.nickname = member.getNickname ();
        this.userImage = userImage;
    }
}
