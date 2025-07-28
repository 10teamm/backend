package com.swyp.catsgotogedog.content.domain.entity;

import com.swyp.catsgotogedog.global.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
public class Content extends BaseTimeEntity {

    @Id
    @Column(nullable = false)
    private int contentId;

    @Column(nullable = false)
    private int categoryId;

    @Column(nullable = false)
    private int regionId;

    private String addr1;

    private String addr2;

    private String image;

    private String thumbImage;

    private String copyright;

    @Column(precision = 10, scale = 8)
    private BigDecimal mapx;

    @Column(precision = 11, scale = 8)
    private BigDecimal mapy;

    private int mlevel;

    private String tel;

    private String title;

    private int zipcode;

    private int contentTypeId;

}
