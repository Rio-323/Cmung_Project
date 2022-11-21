package com.sparta.cmung_project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NaverMemberInfoDto {

    private String naverid;
    private String nickname;
    private String email;
    private String userImage;
}
