package com.sparta.cmung_project.websocket.controller;

import com.sparta.cmung_project.websocket.domain.Chat;
import com.sparta.cmung_project.websocket.dto.ChatReqDto;
import com.sparta.cmung_project.websocket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate template;

    @MessageMapping(value = "{roomId}") //여기로 전송되면 메서드 호출 -> WebSocketConfig prefixes 에서 적용한건 앞에 생략
    @SendTo("/room/{roomId}")   //구독하고 있는 장소로 메시지 전송 (목적지)  -> WebSocketConfig Broker 에서 적용한건 앞에 붙어줘야됨
    public void message(@DestinationVariable Long roomId, ChatReqDto message) {
        template.convertAndSend("/room/" + roomId,message);
        //채팅 저장
        Chat chat = chatService.createChat(roomId, message);

    }

}
