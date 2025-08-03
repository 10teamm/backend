package com.swyp.catsgotogedog.content.service;

import com.swyp.catsgotogedog.content.domain.entity.Content;
import com.swyp.catsgotogedog.content.domain.entity.ContentDocument;
import com.swyp.catsgotogedog.content.domain.entity.ContentImage;
import com.swyp.catsgotogedog.content.domain.request.ContentRequest;
import com.swyp.catsgotogedog.content.domain.response.ContentResponse;
import com.swyp.catsgotogedog.content.domain.response.PlaceDetailResponse;
import com.swyp.catsgotogedog.content.domain.response.RegionCodeResponse;
import com.swyp.catsgotogedog.content.repository.ContentElasticRepository;
import com.swyp.catsgotogedog.content.repository.ContentImageRepository;
import com.swyp.catsgotogedog.content.repository.ContentRepository;
import com.swyp.catsgotogedog.review.domain.entity.ContentReview;
import com.swyp.catsgotogedog.review.repository.ContentReviewRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentService {
    private final ContentRepository contentRepository;
    private final ContentElasticRepository contentElasticRepository;
    private final ContentImageRepository contentImageRepository;
    private final ContentReviewRepository contentReviewRepository;

    private final ContentSearchService contentSearchService;

    public void saveContent(ContentRequest request){
        Content content = Content.builder()
                .categoryId(request.getCategoryId())
                .addr1(request.getAddr1())
                .addr2(request.getAddr2())
                .image(request.getImage())
                .thumbImage(request.getThumbImage())
                .copyright(request.getCopyright())
                .mapx(request.getMapx())
                .mapy(request.getMapy())
                .mLevel(request.getMlevel())
                .tel(request.getTel())
                .title(request.getTitle())
                .zipCode(request.getZipcode())
                .contentTypeId(request.getContentTypeId())
                .build();
        contentRepository.save(content);
        contentElasticRepository.save(ContentDocument.from(content));
    }

    public PlaceDetailResponse getPlaceDetail(int contentId, String userId){

        Content content = contentRepository.findByContentId(contentId);

        ContentImage contentImage = contentImageRepository.findByContent_ContentId(contentId);

        String smallImageUrl = (contentImage != null) ? contentImage.getSmallImageUrl() : null;

        double avg = contentSearchService.getAverageScore(contentId);

        boolean wishData = (userId != null) ? contentSearchService.getWishData(userId, contentId) : false;

        return PlaceDetailResponse.from(content,smallImageUrl,avg,wishData);
    }
}
