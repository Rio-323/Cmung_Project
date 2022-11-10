package com.sparta.cmung_project.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Categorys {
    ALL("전체"),
    L("대형"),
    M("중형"),
    S("소형");


    private String description;
}
