package com.swyp.catsgotogedog.global.exception;

import lombok.Getter;

@Getter
public class ImageUploadException extends CatsgotogedogException {

    public ImageUploadException(ErrorCode errorCode) {
        super(errorCode);
    }

}