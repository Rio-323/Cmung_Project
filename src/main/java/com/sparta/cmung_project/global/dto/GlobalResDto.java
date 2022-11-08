package com.sparta.cmung_project.global.dto;

import com.sparta.cmung_project.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GlobalResDto<T> {

   private int status;
   private T data;
   private T message;

   public static <T> GlobalResDto<Object> success(T data, String message) {
       return new GlobalResDto<> ( 200, data, message );
   }

   public static GlobalResDto<String> fail(ErrorCode errorCode) {
       return new GlobalResDto<> ( errorCode.getStatus (), errorCode.getCode (), errorCode.getMessage () );
   }

   public static GlobalResDto<String> fail(String message) {
       return new GlobalResDto<> ( 400, null, message );
   }
}
