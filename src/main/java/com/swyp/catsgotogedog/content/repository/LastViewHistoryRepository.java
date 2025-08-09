package com.swyp.catsgotogedog.content.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.catsgotogedog.User.domain.entity.User;
import com.swyp.catsgotogedog.content.domain.entity.Content;
import com.swyp.catsgotogedog.mypage.domain.entity.LastViewHistory;

public interface LastViewHistoryRepository extends JpaRepository<LastViewHistory, Integer> {
	Optional<LastViewHistory> findByContentAndUser(Content content, User user);
}
