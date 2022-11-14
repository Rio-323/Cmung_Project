package com.sparta.cmung_project.webSocket.repository;

import com.sparta.cmung_project.webSocket.domain.Chat;
import org.springframework.data.repository.CrudRepository;


import java.util.List;

public interface ChatRepository extends CrudRepository<Chat, Long> {

    List<Chat> findAllByRoomId(Long roomId);
}
