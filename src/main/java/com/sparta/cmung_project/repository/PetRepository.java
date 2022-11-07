package com.sparta.cmung_project.repository;

import com.sparta.cmung_project.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
