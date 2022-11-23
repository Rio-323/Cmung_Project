package com.sparta.cmung_project.websocket.service;


import com.sparta.cmung_project.dto.GlobalResDto;
import com.sparta.cmung_project.exception.CustomException;
import com.sparta.cmung_project.exception.ErrorCode;
import com.sparta.cmung_project.model.Post;
import com.sparta.cmung_project.repository.PostRepository;
import com.sparta.cmung_project.security.user.UserDetailsImpl;
import com.sparta.cmung_project.websocket.controller.RoomReqDto;
import com.sparta.cmung_project.websocket.domain.Chat;
import com.sparta.cmung_project.websocket.domain.Room;
import com.sparta.cmung_project.websocket.repository.ChatRepository;
import com.sparta.cmung_project.websocket.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final ChatRepository chatRepository;

    private final PostRepository postRepository;


    public GlobalResDto<?> joinRoom(Long roomId) {
        //채팅방 내용 불러옴
        List<Chat> chatList = chatRepository.findAllByRoom_PostId(roomId);
        //없으면 처음 입장하는거라 인식 메세지 '입장' 리턴
        if(chatList.isEmpty()){
            return GlobalResDto.success(null,"입장");
        }else{
            return GlobalResDto.success(chatList,"채팅내역");
        }

    }

    //방 만들기
    public GlobalResDto<?> createRoom(RoomReqDto roomReqDto, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(roomReqDto.getPostId()).orElseThrow(
                ()-> new CustomException(ErrorCode.NotFoundPost)
        );

        //이미 만든방이 있다면 room에 저장후 리턴
        Room room = roomRepository.findById(roomReqDto.getPostId())
                //만들어진 방이 없다면 새로 만들어서 리턴
                .orElse(new Room(post.getMember().getId(), roomReqDto,userDetails));


        roomRepository.save(room);
        return GlobalResDto.success(room,null);
    }

    public GlobalResDto<?> roomList(UserDetailsImpl userDetails){
        List<Room> roomList = roomRepository.findAllByJoinUserOrPostUser(userDetails.getMember().getId(),userDetails.getMember().getId());
        if(roomList.isEmpty()){
            return GlobalResDto.fail("채팅 내역이 없습니다");
        }else{
            return GlobalResDto.success(roomList,null);
        }

    }

}
