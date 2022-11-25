package com.sparta.cmung_project.websocket.controller;

import com.sparta.cmung_project.websocket.domain.Chat;
import com.sparta.cmung_project.websocket.dto.ChatReqDto;
import com.sparta.cmung_project.websocket.dto.ChatResDto;
import com.sparta.cmung_project.websocket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate template;

    @MessageMapping(value = "{roomId}") //여기로 전송되면 메서드 호출 -> WebSocketConfig prefixes 에서 적용한건 앞에 생략
    @SendTo("/sub/{roomId}")//구독하고 있는 장소로 메시지 전송 (목적지)  -> WebSocketConfig Broker 에서 적용한건 앞에 붙어줘야됨
    public void message(@DestinationVariable Long roomId,@Valid ChatReqDto message) throws MessagingException {

        //채팅 저장
        Chat chat = chatService.createChat(roomId, message);

        ChatResDto chatResDto = new ChatResDto(message,chat.getSendDate());
        template.convertAndSend("/sub/" + roomId,chatResDto);

    }

}
