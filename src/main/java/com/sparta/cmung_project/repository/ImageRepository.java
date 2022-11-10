package com.sparta.cmung_project.repository;

import com.sparta.cmung_project.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
//    List<Image> findByPostPostId(Long postId);
//    List<Image> findImgByImageId(Long imageId);
//    void deleteByPostPostId(Long postId);
}
