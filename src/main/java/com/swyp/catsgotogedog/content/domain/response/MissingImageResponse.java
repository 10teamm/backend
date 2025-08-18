package com.swyp.catsgotogedog.content.domain.response;

import com.swyp.catsgotogedog.content.domain.entity.Content;

public record MissingImageResponse (
        String title,
        String addr
) {
    public static MissingImageResponse from(Content content) {
        return new MissingImageResponse(content.getTitle(), content.getAddr1());
    }
}
