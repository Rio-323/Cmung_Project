package com.sparta.cmung_project.webSocket.Room;

import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.model.Post;
import com.sparta.cmung_project.model.Timestamped;
import com.sparta.cmung_project.webSocket.chat.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomDetail extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_info_id")
    private RoomInfo roomInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "roomDetail", cascade = CascadeType.REMOVE ,fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Chat> chats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postid", nullable = false)
    private Post post;

    public RoomDetail(RoomInfo roomInfo, Member member,Post post) {
        this.roomInfo = roomInfo;
        this.member = member;
        this.post = post;
    }

    @Column
    private Long chatId;

    public void updateChatId(Long chatId) {
        this.chatId = chatId;
    }
}
