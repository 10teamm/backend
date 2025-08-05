package com.swyp.catsgotogedog.content.domain.response;

import com.swyp.catsgotogedog.content.domain.entity.Content;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ContentResponse {
    private int contentId;
    private String title;
    private String addr1;
    private String addr2;
    private String image;
    private String thumbImage;
    private String categoryId;
    private int contentTypeId;
    private String copyright;
    private double mapx;
    private double mapy;
    private int mlevel;
    private String tel;
    private int zipcode;

    private Double avgScore;

    private boolean wishData;

    private RegionCodeResponse regionName;

    public static ContentResponse from(
            Content c,
            Double avgScore,
            boolean wishData,
            RegionCodeResponse regionName){

        return ContentResponse.builder()
                .contentId(c.getContentId())
                .title(c.getTitle())
                .addr1(c.getAddr1())
                .addr2(c.getAddr2())
                .image(c.getImage())
                .thumbImage(c.getThumbImage())
                .categoryId(c.getCategoryId())
                .contentTypeId(c.getContentTypeId())
                .copyright(c.getCopyright())
                .mapx(c.getMapx())
                .mapy(c.getMapy())
                .mlevel(c.getMLevel())
                .tel(c.getTel())
                .zipcode(c.getZipCode())
                .avgScore(avgScore)
                .wishData(wishData)
                .regionName(regionName)
                .build();
    }
}
