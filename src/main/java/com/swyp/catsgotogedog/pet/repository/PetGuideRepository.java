package com.swyp.catsgotogedog.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.catsgotogedog.pet.domain.entity.PetGuide;

public interface PetGuideRepository extends JpaRepository<PetGuide, Integer> {
}
