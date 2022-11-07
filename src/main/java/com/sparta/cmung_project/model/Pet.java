package com.sparta.cmung_project.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter // set 함수를 일괄적으로 만들어줍니다.
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String category;

    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    public Pet(String name, int age, String category, Member member) {
        this.name = name;
        this.age = age;
        this.category = category;
        this.member = member;
    }

    public void update(String name, int age, String category) {
        this.name = name;
        this.age = age;
        this.category = category;
    }
}
