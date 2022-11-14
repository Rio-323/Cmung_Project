package com.sparta.cmung_project.webSocket.repository;

import com.sparta.cmung_project.webSocket.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoomRepository extends JpaRepository<Room, Long> {
}
