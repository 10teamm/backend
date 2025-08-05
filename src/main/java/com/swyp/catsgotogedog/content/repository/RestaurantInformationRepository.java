package com.swyp.catsgotogedog.content.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.catsgotogedog.content.domain.entity.batch.information.RestaurantInformation;
import org.springframework.data.jpa.repository.Query;

public interface RestaurantInformationRepository extends JpaRepository<RestaurantInformation, Integer> {
    @Query("select r.restDate from RestaurantInformation r where r.content.contentId = :contentId")
    String findRestDateByContentId(int contentId);
}
