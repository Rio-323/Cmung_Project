package com.sparta.cmung_project.webSocket.Controller;



import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.security.user.UserDetailsImpl;
import com.sparta.cmung_project.webSocket.Room.RoomDetail;
import com.sparta.cmung_project.webSocket.Service.ChatService;
import com.sparta.cmung_project.webSocket.Repository.RoomDetailRepository;
import com.sparta.cmung_project.webSocket.Repository.RoomInfoRepository;
import com.sparta.cmung_project.webSocket.dto.ChatDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@AllArgsConstructor
public class ChatController {
    private final RoomInfoRepository roomInfoRepository;
    private final RoomDetailRepository roomDetailRepository;
    private final ChatService chatService;


    /*채팅방과 연결*/
    @GetMapping("/room/{itemId}")
    public ResponseEntity<?> getRoomChat(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @PathVariable Long itemId) {
        Member member = userDetails.getMember();
        RoomDetail roomDetail = roomDetailRepository.findByMember_IdAndPost_Id(member.getId(), itemId)
                .orElseThrow();
    List<ChatDto> chats = chatService.getChat(roomDetail.getRoomInfo());
        return ResponseEntity.ok().body(chats);
    }
}

//    @GetMapping("/chat")
//    public String chatGET(){
//
//        log.info("@ChatController, chat GET()");
//
//        return "chat";
//    }