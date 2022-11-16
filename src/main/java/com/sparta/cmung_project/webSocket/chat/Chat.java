package com.sparta.cmung_project.webSocket.chat;

import com.sparta.cmung_project.model.Timestamped;
import com.sparta.cmung_project.webSocket.Room.RoomDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chat extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_detail_id", nullable = false)
    private RoomDetail roomDetail;

    @Column
    private Long roomInfoId;
//
//    @Column
//    private String proPic;


    @Column(nullable = false)
    private String message;
}
