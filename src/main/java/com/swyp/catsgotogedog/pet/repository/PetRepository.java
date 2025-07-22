package com.swyp.catsgotogedog.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.catsgotogedog.pet.domain.entity.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
