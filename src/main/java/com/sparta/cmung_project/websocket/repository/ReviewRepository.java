package com.sparta.cmung_project.websocket.repository;


import com.sparta.cmung_project.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {


    Long countByMember_Id(Long memberId);
}
