package com.swyp.catsgotogedog.content.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
public class ContentWish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int wishId;

    private int userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;
}
