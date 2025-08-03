package com.swyp.catsgotogedog.content.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.swyp.catsgotogedog.content.domain.entity.ContentImage;

public interface ContentImageRepository extends JpaRepository<ContentImage, Integer> {
  ContentImage findByContent_ContentId(int contentId);
}
