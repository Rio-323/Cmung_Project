package com.sparta.cmung_project.websocket.repository;

import com.sparta.cmung_project.websocket.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findAllByRoom_PostId(Long roomId);
}
