package com.swyp.catsgotogedog.content.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class ContentImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int contentImageId;

    private int contentId;

    private String imageUrl;

    private String imageFilename;

    private String smallImageUrl;
}
