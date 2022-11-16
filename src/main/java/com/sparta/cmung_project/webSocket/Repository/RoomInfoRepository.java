package com.sparta.cmung_project.webSocket.Repository;



import com.sparta.cmung_project.webSocket.Room.RoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomInfoRepository extends JpaRepository<RoomInfo, Long> {
    Optional<RoomInfo> findByMember_MemberIdAndItem_Id(Long memberId, Long itemId);
}