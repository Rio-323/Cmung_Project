package com.sparta.cmung_project.webSocket.Repository;


import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.webSocket.Room.RoomDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomDetailRepository extends JpaRepository<RoomDetail, Long> {

    List<RoomDetail>findAllByMemberOrderByModifiedAtDesc(Member member);
    Optional<RoomDetail> findByRoomInfo_IdAndMember_MemberIdAndItem_Id(Long infoId, Long memberId, Long itemId);
    Optional<RoomDetail> findByMember_MemberIdAndItem_Id(Long memberId, Long itemId);
    Optional<RoomDetail> findByRoomInfo_IdAndMember_MemberId(Long infoId, Long memberId);
}
