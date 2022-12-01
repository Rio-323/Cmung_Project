package com.sparta.cmung_project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberResponseDto {
    private Long id;
    private String nickname;
    private String userImage;

    private Long rating;

    private Long kakaoId;

    private String naverId;

    public MemberResponseDto(Long id, String nickname,Long rating, String userImage, Long kakaoId, String naverId) {
        this.id = id;
        this.nickname = nickname;
        this.rating = rating;
        this.userImage = userImage;
        this.kakaoId = kakaoId;
        this.naverId = naverId;
    }
}
