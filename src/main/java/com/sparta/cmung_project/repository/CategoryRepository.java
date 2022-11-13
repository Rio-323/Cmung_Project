package com.sparta.cmung_project.repository;

import com.sparta.cmung_project.model.Category;
import com.sparta.cmung_project.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
