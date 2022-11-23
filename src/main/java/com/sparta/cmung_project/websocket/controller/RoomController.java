package com.sparta.cmung_project.websocket.controller;

import com.sparta.cmung_project.dto.GlobalResDto;
import com.sparta.cmung_project.security.user.UserDetailsImpl;
import com.sparta.cmung_project.websocket.domain.Chat;
import com.sparta.cmung_project.websocket.domain.Room;
import com.sparta.cmung_project.websocket.service.ChatService;
import com.sparta.cmung_project.websocket.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RestController
@RequiredArgsConstructor
public class RoomController {

    private final ChatService chatService;
    private final RoomService roomService;


    @GetMapping("/room/{roomId}")
    public GlobalResDto<?> joinRoom(@PathVariable(required = false) Long roomId){
//        model.addAttribute("roomId", roomId);
//        model.addAttribute("chatList", chatList);
        return roomService.joinRoom(roomId);
    }


    /**
     * 채팅방 등록
     */
    @PostMapping("/room")
    public GlobalResDto<?> createRoom(@RequestBody RoomReqDto roomReqDto,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info(roomReqDto.getPostId().toString());
        log.info(roomReqDto.getPostTitle());
        return roomService.createRoom(roomReqDto,userDetails);
    }

    /**
     * 채팅방 리스트 보기
     */
    @GetMapping("/roomList")
    public GlobalResDto<?> roomList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return roomService.roomList(userDetails);
    }



}
