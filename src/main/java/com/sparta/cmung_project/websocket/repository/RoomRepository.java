package com.sparta.cmung_project.websocket.repository;

import com.sparta.cmung_project.websocket.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RoomRepository extends JpaRepository<Room, Long> {


    List<Room> findAllByJoinUserOrPostUser(Long postId, Long joinId);

    List<Room> findRoomByPostUserAndJoinUser(Long postId, Long joinId);
}
