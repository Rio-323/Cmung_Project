package com.sparta.cmung_project.websocket.controller;

import com.sparta.cmung_project.dto.GlobalResDto;
import com.sparta.cmung_project.security.user.UserDetailsImpl;
import com.sparta.cmung_project.websocket.dto.RatingReqDto;
import com.sparta.cmung_project.websocket.dto.RoomReqDto;
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


    @GetMapping("/roomInfo/{roomId}")
    public GlobalResDto<?> joinRoom(@PathVariable Long roomId,@AuthenticationPrincipal UserDetailsImpl userDetails){
//        model.addAttribute("roomId", roomId);
//        model.addAttribute("chatList", chatList);
        return roomService.joinRoom(roomId, userDetails);
    }


    /**
     * 채팅방 등록
     */
    @PostMapping("/room")
    public GlobalResDto<?> createRoom(@RequestBody RoomReqDto roomReqDto,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info(roomReqDto.getPostId().toString());
        log.info(roomReqDto.getPostTitle());
        return roomService.createRoom(roomReqDto, userDetails);
    }

    /**
     * 채팅방 리스트 보기
     */
    @GetMapping("/roomList")
    public GlobalResDto<?> roomList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return roomService.roomList(userDetails);
    }


    @PutMapping("/room/{postId}")
    public GlobalResDto<String> stateUpdate(@PathVariable Long postId){
        return roomService.stateUpdate(postId);
    }

    @PutMapping("/rating")
    public GlobalResDto<?> rating(@RequestBody RatingReqDto ratingReqDto,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails){
        return roomService.rating(ratingReqDto,userDetails);
    }
}
