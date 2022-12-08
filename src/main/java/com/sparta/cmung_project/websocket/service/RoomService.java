package com.sparta.cmung_project.websocket.service;


import com.sparta.cmung_project.dto.GlobalResDto;
import com.sparta.cmung_project.exception.CustomException;
import com.sparta.cmung_project.exception.ErrorCode;
import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.model.Post;
import com.sparta.cmung_project.model.Review;
import com.sparta.cmung_project.repository.MemberRepository;
import com.sparta.cmung_project.repository.PostRepository;
import com.sparta.cmung_project.security.user.UserDetailsImpl;
import com.sparta.cmung_project.websocket.dto.RatingReqDto;
import com.sparta.cmung_project.websocket.dto.RoomReqDto;
import com.sparta.cmung_project.websocket.domain.Room;
import com.sparta.cmung_project.websocket.dto.RoomResponseDto;
import com.sparta.cmung_project.websocket.repository.ChatRepository;
import com.sparta.cmung_project.websocket.repository.ReviewRepository;
import com.sparta.cmung_project.websocket.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final ChatRepository chatRepository;

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;


    public GlobalResDto<?> joinRoom( Long roomId, UserDetailsImpl userDetails) {


        Room room = roomRepository.findById(roomId).orElseThrow(
                ()-> new CustomException(ErrorCode.NotfoundRoom)
        );
        Post post = postRepository.findById(room.getPost().getId()).orElseThrow(
                ()-> new CustomException(ErrorCode.NotFoundPost)
        );
        room.stateUpdate(post);

        RoomResponseDto roomResponseDto = new RoomResponseDto(room);
        return GlobalResDto.success(roomResponseDto,room.getId()+"번방");
    }

    //방 만들기
    public GlobalResDto<?> createRoom(RoomReqDto roomReqDto, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(roomReqDto.getPostId()).orElseThrow(
                ()-> new CustomException(ErrorCode.NotFoundPost)
        );
//        if(post.getMember().getId().equals(userDetails.getMember().getId())){
//            throw new CustomException(ErrorCode.SameUser);
//        }
        //이미 만든방이 있다면 room에 저장후 리턴
        Room room = roomRepository.findRoomByJoinUser_IdAndPostUser_IdAndPostId(userDetails.getMember().getId(), post.getMember().getId(), roomReqDto.getPostId())
                //만들어진 방이 없다면 새로 만들어서 리턴
                .orElse(new Room(post.getMember(), userDetails.getMember(), roomReqDto, post));

        roomRepository.save(room);
        RoomResponseDto roomResponseDto = new RoomResponseDto(room);
        return GlobalResDto.success(roomResponseDto,null);
    }

    public GlobalResDto<?> roomList(UserDetailsImpl userDetails){
        List<Room> roomList = roomRepository.findAllByJoinUser_IdOrPostUser_IdOrderByIdDesc(userDetails.getMember().getId(),userDetails.getMember().getId());
        List<RoomResponseDto> roomResponseDtos = new ArrayList<>();

        for(Room room : roomList ){
            roomResponseDtos.add(new RoomResponseDto(room));
        }

        if(roomList.isEmpty()){
            return GlobalResDto.fail("채팅 내역이 없습니다");
        }else{
            return GlobalResDto.success(roomResponseDtos,null);
        }

    }

    //거래상태 변경
    @Transactional
    public GlobalResDto<String> stateUpdate(Long id){
        Post post = postRepository.findById(id).orElseThrow(
                ()-> new CustomException(ErrorCode.NotFoundPost)
        );
        if("산책중".equals(post.getState())){
            post.stateUpdate("완료");
            return GlobalResDto.success(null,"완료");
        }else{
            post.stateUpdate("산책중");
            return GlobalResDto.success(null,"진행중");
        }
    }


    @Transactional
    public GlobalResDto<?> rating(RatingReqDto ratingReqDto,UserDetailsImpl userDetails){
        Member member = memberRepository.findById(ratingReqDto.getJoinUser()).orElseThrow(
                ()-> new CustomException(ErrorCode.NotFoundMember)
        );
        Review review = new Review(member,ratingReqDto.getRating(),userDetails.getMember().getId());

        reviewRepository.save(review);

        Long count =  reviewRepository.countByMember_Id(ratingReqDto.getJoinUser());

        Long score = (member.getSum() + ratingReqDto.getRating()) / count;
        member.setRating(score);
        member.setSum(ratingReqDto.getRating());
        log.info(Long.toString(count));
        log.info(Long.toString(score));
        return GlobalResDto.success(score, "작성완료");





    }

}
