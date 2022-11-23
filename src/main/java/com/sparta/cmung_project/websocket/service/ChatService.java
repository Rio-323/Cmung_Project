package com.sparta.cmung_project.websocket.service;

import com.sparta.cmung_project.exception.CustomException;
import com.sparta.cmung_project.exception.ErrorCode;
import com.sparta.cmung_project.model.Post;
import com.sparta.cmung_project.repository.PostRepository;
import com.sparta.cmung_project.security.user.UserDetailsImpl;
import com.sparta.cmung_project.websocket.controller.RoomReqDto;
import com.sparta.cmung_project.websocket.domain.Chat;
import com.sparta.cmung_project.websocket.domain.Room;
import com.sparta.cmung_project.websocket.dto.ChatReqDto;
import com.sparta.cmung_project.websocket.repository.ChatRepository;
import com.sparta.cmung_project.websocket.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RoomRepository roomRepository;
    private final ChatRepository chatRepository;



    /**
     * 채팅 생성
     */
    public Chat createChat(Long roomId, ChatReqDto chatReqDto) {
        Room room = roomRepository.findById(roomId).orElseThrow(
                ()-> new CustomException(ErrorCode.NotFoundPost)
        );//방 찾기 -> 없는 방일 경우 여기서 예외처리
          Chat chat = Chat.builder()
                .room(room)
                .sender(chatReqDto.getSender())
                .message(chatReqDto.getMessage())
                .build();
          chatRepository.save(chat);
        return chat;
    }

    /**
     * 채팅방 채팅내용 불러오기
     * @param roomId 채팅방 id
     */
    public List<Chat> findAllChatByRoomId(Long roomId) {
        return chatRepository.findAllByRoom_PostId(roomId);
    }


}
