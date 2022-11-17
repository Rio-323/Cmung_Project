package com.sparta.cmung_project.webSocket.Repository;



import com.sparta.cmung_project.webSocket.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat>findByRoomDetail_Member_IdOrderByCreatedAtDesc(Long memberId);
    List<Chat>findByRoomDetail_RoomInfo_IdOrderByCreatedAtDesc(Long roomInfoId);
    Optional<Chat> findFirstByRoomDetail_RoomInfo_IdOrderByCreatedAtDesc(Long roomInfoId);

    List<Chat>findByRoomDetail_RoomInfo_IdOrderByCreatedAtAsc(Long roomInfoId);


}
