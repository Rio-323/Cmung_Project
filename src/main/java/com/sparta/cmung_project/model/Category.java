package com.sparta.cmung_project.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.cmung_project.dto.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter // set 함수를 일괄적으로 만들어줍니다.
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    List<Post> postList = new ArrayList<>();


    public Category(String name) {
        this.name = name;
    }
}
