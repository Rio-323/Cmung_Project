package com.sparta.cmung_project.webSocket.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;


    private Long postId;


    private Long joinUserId;



   public Room(Long postId, Long joinUserId){
       this.postId = postId;
       this.joinUserId = joinUserId;
   }

}