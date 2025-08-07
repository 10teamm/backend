package com.swyp.catsgotogedog.mypage.domain.response;

public record ContentWishResponse (
	String imageUrl,
	String thumbnailUrl,
	Boolean isWish
) {
}
