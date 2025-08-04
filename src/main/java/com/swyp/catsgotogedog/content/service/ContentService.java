package com.swyp.catsgotogedog.content.service;

import com.swyp.catsgotogedog.User.domain.entity.User;
import com.swyp.catsgotogedog.User.repository.UserRepository;
import com.swyp.catsgotogedog.content.domain.entity.Content;
import com.swyp.catsgotogedog.content.domain.entity.ContentDocument;
import com.swyp.catsgotogedog.content.domain.entity.ContentImage;
import com.swyp.catsgotogedog.content.domain.entity.ViewLog;
import com.swyp.catsgotogedog.content.domain.request.ContentRequest;
import com.swyp.catsgotogedog.content.domain.response.LastViewHistoryResponse;
import com.swyp.catsgotogedog.content.domain.response.PlaceDetailResponse;
import com.swyp.catsgotogedog.content.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentService {
    private final ContentRepository contentRepository;
    private final ContentElasticRepository contentElasticRepository;
    private final ContentImageRepository contentImageRepository;
    private final ContentWishRepository contentWishRepository;
    private final ViewTotalRepository viewTotalRepository;
    private final UserRepository userRepository;
    private final ViewLogRepository viewLogRepository;
    private final VisitHistoryRepository visitHistoryRepository;

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

        viewTotalRepository.upsertAndIncrease(contentId);

        if(userId != null){
            recordView(userId, contentId);
        }

        Content content = contentRepository.findByContentId(contentId);

        ContentImage contentImage = contentImageRepository.findByContent_ContentId(contentId);

        String smallImageUrl = (contentImage != null) ? contentImage.getSmallImageUrl() : null;

        double avg = contentSearchService.getAverageScore(contentId);

        boolean wishData = (userId != null) ? contentSearchService.getWishData(userId, contentId) : false;

        int wishCnt = contentWishRepository.countByContentId(contentId);

        boolean visited = hasVisited(userId, contentId);

        return PlaceDetailResponse.from(content,smallImageUrl,avg,wishData,wishCnt,visited);
    }

    public void recordView(String userId, int contentId){

//        if (userId != null) {
//            Optional<User> user = userRepository.findById(Integer.parseInt(userId));
//        }

        Content content = contentRepository.findByContentId(contentId);
        if (content == null) {
            throw new EntityNotFoundException("contentId=" + contentId);
        }

        User user = null;
        if (userId != null && !userId.isBlank()) {
            user = userRepository.findById(Integer.parseInt(userId))
                    .orElseThrow(() -> new EntityNotFoundException("userId=" + userId));
        }

        viewLogRepository.save(
                ViewLog.builder()
                        .user(user)
                        .content(content)
                        .build()
        );
    }

    public List<LastViewHistoryResponse> getRecentViews(String userId) {

        if (userId == null || userId.isBlank()) {
            return null;
        }

        Pageable top = PageRequest.of(0, 20);
        List<Content> contents = viewLogRepository.findRecentContentByUser(Integer.parseInt(userId), top);

        return contents.stream()
                .map(LastViewHistoryResponse::from)
                .toList();
    }

    public boolean hasVisited(String userId, int contentId) {
        if (userId == null || userId.isBlank()) {
            return false;
        }
        return visitHistoryRepository.existsByUser_IdAndContent_ContentId(Integer.parseInt(userId), contentId);
    }
}
