package com.sparta.cmung_project.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sparta.cmung_project.websocket.dto.RatingReqDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter // set 함수를 일괄적으로 만들어줍니다.
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonBackReference
    @ManyToOne
    @JoinColumn( name="memberId" , nullable = false)
    private Member member;

    private Long score;

    private Long reviewer;

    public Review(Member member,Long score,Long reviewer){
        this.member = member;
        this.score = score;
        this.reviewer = reviewer;
    }

}
