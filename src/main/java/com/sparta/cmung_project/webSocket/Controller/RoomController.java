package com.sparta.cmung_project.webSocket.Controller;


import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.security.user.UserDetailsImpl;
import com.sparta.cmung_project.webSocket.Repository.RoomInfoRepository;
import com.sparta.cmung_project.webSocket.Room.Dto.RoomInfoRequestDto;
import com.sparta.cmung_project.webSocket.Room.Dto.RoomInfoResponseDto;
import com.sparta.cmung_project.webSocket.Room.Dto.RoomInviteDto;
import com.sparta.cmung_project.webSocket.Room.RoomInfo;
import com.sparta.cmung_project.webSocket.Service.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = {"채팅방 API 정보를 제공하는 Controller"})
@RequiredArgsConstructor
@RestController
public class RoomController {

    private final RoomService roomService;
    private final RoomInfoRepository roomInfoRepository;

    @ApiOperation(value = "채팅방 생성 메소드")
    @PostMapping(value = "/roomInfo")
    public ResponseEntity<?> createRoom(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @RequestBody RoomInfoRequestDto requestDto) {
        Member member = userDetails.getMember();
        return ResponseEntity.ok().body(Map.of("msg", "성공","roomInfo",roomService.createRoom(member,requestDto)));
    }

    @ApiOperation(value = "채팅방 정보 조회 메소드")
    @GetMapping(value = "/roomInfo")
    public ResponseEntity<?> getRoomInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        List<RoomInfoResponseDto> ResponseDtos = roomService.getRoomInfo(member);
        return ResponseEntity.ok().body(ResponseDtos);
    }

    @ApiOperation(value = "채팅방 나가기")
    @DeleteMapping(value = "/rooms/{itemId}")
    public ResponseEntity<?> deleteRoomInfo(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @PathVariable Long itemId) {
        Member member = userDetails.getMember();
        RoomInfo roomInfo = roomInfoRepository.findByMember_IdAndPost_Id(member.getId(), itemId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 채팅방 입니다."));
        roomService.deleteRoomInfo(member, roomInfo.getId());
        return ResponseEntity.ok().body(Map.of("success", true, "msg", "삭제 성공"));

    }

    @ApiOperation(value = "채팅방 입장 메소드")
    @PostMapping(value = "/rooms/{roomInfoId}")
    public ResponseEntity<?> inviteRoom(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @PathVariable Long roomInfoId, @RequestBody RoomInviteDto inviteDto) {
        Member member = userDetails.getMember();
        roomService.inviteRoom(member, roomInfoId, inviteDto);
        return ResponseEntity.ok().body(Map.of("msg", "초대 성공"));
    }


}
