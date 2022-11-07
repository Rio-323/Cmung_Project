package com.sparta.cmung_project.model;

import com.sparta.cmung_project.dto.MemberResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter // set 함수를 일괄적으로 만들어줍니다.
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userImage;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Post> post;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Pet> pet;

    public MemberResponseDto toDto() {
        return new MemberResponseDto(this.id, this.nickname, this.userImage);
    }
}
