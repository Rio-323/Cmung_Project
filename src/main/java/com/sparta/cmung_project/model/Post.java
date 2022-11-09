package com.sparta.cmung_project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.cmung_project.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter // set 함수를 일괄적으로 만들어줍니다.
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String state;

    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    @JsonBackReference
    private Member member;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    @JsonBackReference
    private Category category;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Image> image;

    public Post(PostRequestDto postRequestDto, Category category,Member member){
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.price = postRequestDto.getPrice();
        this.category = category;
        this.member = member;
    }

    public void update (PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.category = postRequestDto.getCategory();
        this.price = postRequestDto.getPrice();
    }
//    public PostResponseDto toDto() {
//        return new PostResponseDto(this.id, this.title, this.content, this.type);
//    }
}
