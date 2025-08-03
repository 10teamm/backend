package com.swyp.catsgotogedog.content.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.catsgotogedog.content.domain.entity.batch.information.RestaurantInformation;

public interface RestaurantInformationRepository extends JpaRepository<RestaurantInformation, Integer> {
}
