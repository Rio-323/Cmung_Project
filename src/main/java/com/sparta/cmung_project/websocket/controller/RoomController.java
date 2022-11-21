package com.sparta.cmung_project.websocket.controller;

import com.sparta.cmung_project.security.user.UserDetailsImpl;
import com.sparta.cmung_project.websocket.domain.Chat;
import com.sparta.cmung_project.websocket.domain.Room;
import com.sparta.cmung_project.websocket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoomController {

    private final ChatService chatService;

    /**
     * 채팅방 참여하기
     * @param roomId 채팅방 id
     */
    @GetMapping("/{roomId}")
    public List<Chat> joinRoom(@PathVariable(required = false) Long roomId, Model model) {
        List<Chat> chatList = chatService.findAllChatByRoomId(roomId);
        Room room = chatService.findRoomById(roomId);
        model.addAttribute("roomId", roomId);
        model.addAttribute("chatList", chatList);
        return chatList;
    }

    /**
     * 채팅방 등록
     */
    @PostMapping("/room")
    public Room createRoom(@RequestBody RoomReqDto roomReqDto,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return chatService.createRoom(roomReqDto,userDetails);
    }

    /**
     * 채팅방 리스트 보기
     */
    @GetMapping("/roomList")
    public List<Room> roomList(Model model) {
        List<Room> roomList = chatService.findAllRoom();
        model.addAttribute("roomList", roomList);
        return roomList;
    }



}
