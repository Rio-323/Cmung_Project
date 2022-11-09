package com.sparta.cmung_project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GlobalResDto<T> {
    private Boolean success;
    private T data;
    private String msg;

    public static <T> GlobalResDto <T> success(T data,String msg){
        return new GlobalResDto<>(true, data, msg);
    }

    public static <T> GlobalResDto <T> fail(String msg){
        return new GlobalResDto<>(false, null, msg);
    }
}