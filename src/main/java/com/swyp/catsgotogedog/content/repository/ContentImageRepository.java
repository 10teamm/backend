package com.swyp.catsgotogedog.content.repository;

import com.swyp.catsgotogedog.content.domain.entity.ContentImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentImageRepository extends JpaRepository<ContentImage, Integer> {
    ContentImage findByContentId(int contentId);
}
