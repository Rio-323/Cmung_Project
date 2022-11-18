package com.sparta.cmung_project.websocket.repository;

import com.sparta.cmung_project.websocket.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoomRepository extends JpaRepository<Room, Long> {
}
