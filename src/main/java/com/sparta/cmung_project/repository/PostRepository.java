package com.sparta.cmung_project.repository;

import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);
    List<Post> findAllByTitleContainingOrContentContainingOrLocalContainingOrderByCreatedAtDesc(String searchKeyword1, String searchKeyword2, String searchKeyword3);
    Post findByIdAndMember(Long id, Member member);
    List<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    List<Post> findAllByMemberOrderByCreatedAtDesc(Member member);
    List<Post> findAllByMember(Member member);
    List<Post> findAll();
    List<Post> findAllByCategory_NameOrderByCreatedAtDesc(String name, Pageable pageable);
}
