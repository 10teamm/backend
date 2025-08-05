package com.swyp.catsgotogedog.content.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.catsgotogedog.content.domain.entity.batch.information.SightsInformation;
import org.springframework.data.jpa.repository.Query;

public interface SightsInformationRepository extends JpaRepository<SightsInformation, Integer> {
    @Query("select s.restDate from SightsInformation s where s.content.contentId = :contentId")
    String findRestDateByContentId(int contentId);
}
