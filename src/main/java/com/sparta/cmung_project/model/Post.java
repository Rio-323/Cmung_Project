package com.sparta.cmung_project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.cmung_project.dto.PostRequestDto;
import com.sparta.cmung_project.dto.PostResponseDto;

import com.sparta.cmung_project.time.Time;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Setter // set 함수를 일괄적으로 만들어줍니다.
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String nickname;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String local;

    @Column(nullable = false)
    private String date;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @JsonManagedReference
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Image> image;

    public Post(PostRequestDto postRequestDto, Category category, Member member) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.nickname = member.getNickname();
        this.price = postRequestDto.getPrice();
        this.category = category;
        this.member = member;
        this.state = postRequestDto.getState();
        this.local = postRequestDto.getLocal();
        this.date = postRequestDto.getDate();
    }

    public void update (PostRequestDto postRequestDto, Category category) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.category = category;
        this.state = postRequestDto.getState();
        this.price = postRequestDto.getPrice();
        this.local = postRequestDto.getLocal();
        this.date = postRequestDto.getDate();

    }
    public void stateUpdate(String state){
        this.state = state;
    }

    public PostResponseDto toDto() {
        // 이미지 객체 리스트를 문자열 리스트로 변환
        List<String> imageList = this.image
                .stream().map((imageObj) -> {
                    // 포스트 객체를 DTO로 만든다.
                    String imageStr = imageObj.getImage();

                    // DTO 반환
                    return imageStr;
                })
                .collect(Collectors.toList());
        // 날자 설정
        Date date = Timestamp.valueOf(super.getCreatedAt());
        String dateString = Time.calculateTime(date);


        // DTO 반환
        return new PostResponseDto(this.id, this.title, this.content, member.getRating(), member.getUserImage(), this.price,
                this.category.getName(), this.state, this.local, this.date, imageList,
                dateString, this.nickname);
    }
}
