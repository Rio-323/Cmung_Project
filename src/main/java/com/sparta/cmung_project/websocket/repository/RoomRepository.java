package com.sparta.cmung_project.websocket.repository;

import com.sparta.cmung_project.websocket.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface RoomRepository extends JpaRepository<Room, Long> {


    List<Room> findAllByJoinUser_IdOrPostUser_IdOrderByIdDesc(Long postId, Long joinId);

    Optional<Room> findRoomByJoinUser_IdAndPostUser_IdAndPostId(Long joinUser, Long postUser, Long postId);


}
