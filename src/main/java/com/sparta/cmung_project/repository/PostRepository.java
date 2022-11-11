package com.sparta.cmung_project.repository;

import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);

    List<Post> findAllByTitleContainingOrContentContaining(String searchKeyword, String searchKeyword1);
    Post findByIdAndMember(Long id, Member member);
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findAll();
}
