package com.sparta.cmung_project.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatReqDto {

    @NotNull(message = "값이 없음")
    private String sender;
    @NotNull(message = "값이 없음")
    private String message;

}
