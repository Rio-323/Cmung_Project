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

    public MemberResponseDto(Long id, String nickname, String userImage) {
        this.id = id;
        this.nickname = nickname;
        this.userImage = userImage;
    }
}
