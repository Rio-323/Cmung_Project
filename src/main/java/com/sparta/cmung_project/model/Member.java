package com.sparta.cmung_project.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.cmung_project.dto.MemberReqDto;
import com.sparta.cmung_project.dto.MemberResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.List;

@Setter // set 함수를 일괄적으로 만들어줍니다.
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
public class Member {
    @Id
    @Column(name = "memberId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String userImage;

    @JsonManagedReference
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Post> post;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Pet> pet;

    public MemberResponseDto toDto() {
        return new MemberResponseDto(this.id, this.nickname, this.userImage);
    }

    public Member(MemberReqDto memberReqDto) {
        this.userId = memberReqDto.getUserId();
        this.password = memberReqDto.getPassword();
        this.nickname = memberReqDto.getNickname ();
    }

    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return !passwordEncoder.matches ( password, this.password );
    }

    public void setEncodePassword(String encodePassword) { this.password = encodePassword; }
}
