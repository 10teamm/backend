package com.swyp.catsgotogedog.mypage.domain.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LastViewHistoryId implements Serializable {

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "content_id")
	private Integer contentId;
}
