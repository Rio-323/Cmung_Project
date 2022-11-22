package com.sparta.cmung_project.repository;

import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Optional<Pet>> findByMember(Member member);
}
