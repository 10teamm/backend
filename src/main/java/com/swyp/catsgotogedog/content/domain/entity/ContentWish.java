package com.swyp.catsgotogedog.content.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class ContentWish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int wishId;

    private int userId;

    private int contentId;
}
