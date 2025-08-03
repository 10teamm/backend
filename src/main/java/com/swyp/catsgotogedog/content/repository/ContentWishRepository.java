package com.swyp.catsgotogedog.content.repository;

import com.swyp.catsgotogedog.content.domain.entity.ContentWish;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentWishRepository extends JpaRepository<ContentWish, Integer> {
    Optional<ContentWish> findByUserIdAndContentId(int userId, int contentId);
}
