package com.sparta.cmung_project.webSocket.Room;


import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.model.Post;
import com.sparta.cmung_project.model.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomInfo extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "roomInfo", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<RoomDetail> roomDetail;


    @ManyToOne
    @JoinColumn(name = "itemId")
    private Post post;

    @ManyToOne
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String recentChat;

    public void updateRecentChat(String recentChat) {
        this.recentChat = recentChat;
    }
}
