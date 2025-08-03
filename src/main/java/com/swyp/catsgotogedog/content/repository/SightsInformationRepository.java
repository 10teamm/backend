package com.swyp.catsgotogedog.content.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.catsgotogedog.content.domain.entity.batch.information.SightsInformation;

public interface SightsInformationRepository extends JpaRepository<SightsInformation, Integer> {
}
