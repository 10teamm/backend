package com.swyp.catsgotogedog.content.domain.response;

import com.swyp.catsgotogedog.content.domain.entity.Content;
import lombok.Builder;

@Builder
public record PlaceDetailResponse(
        int contentId,
        String title,
        String addr1,
        String addr2,
        String image,
        String thumbImage,
        String categoryId,
        int contentTypeId,
        String copyright,
        double mapx,
        double mapy,
        int mlevel,
        String tel,
        int zipcode,
        String smallImageUrl,
        Double avgScore,
        boolean wishData,
        int wishCnt,
        boolean visited) {

    public static PlaceDetailResponse from(
            Content c,
            String smallImageUrl,
            Double avgScore,
            boolean wishData,
            int wishCnt,
            boolean visited){

        return PlaceDetailResponse.builder()
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
                .smallImageUrl(smallImageUrl)
                .avgScore(avgScore)
                .wishData(wishData)
                .wishCnt(wishCnt)
                .visited(visited)
                .build();
    }
}
