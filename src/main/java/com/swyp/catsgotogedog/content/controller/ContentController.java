package com.swyp.catsgotogedog.content.controller;

import com.swyp.catsgotogedog.content.domain.request.ContentRequest;
import com.swyp.catsgotogedog.content.domain.response.ContentResponse;
import com.swyp.catsgotogedog.content.domain.response.LastViewHistoryResponse;
import com.swyp.catsgotogedog.content.domain.response.PlaceDetailResponse;
import com.swyp.catsgotogedog.content.service.ContentSearchService;
import com.swyp.catsgotogedog.content.service.ContentService;
import org.apache.commons.lang3.math.NumberUtils;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
public class ContentController implements ContentControllerSwagger{
    private final ContentService contentService;
    private final ContentSearchService contentSearchService;

    @GetMapping("/search")
    public ResponseEntity<List<ContentResponse>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String sido,
            @RequestParam(required = false) String sigungu,
            @RequestParam(required = false) Integer contentTypeId,
            @AuthenticationPrincipal String principal) {

        String userId = null;
        if (StringUtils.hasText(principal) && NumberUtils.isCreatable(principal)) {
            userId = principal;
        }

        List<ContentResponse> list = contentSearchService.search(title, sido, sigungu, contentTypeId, userId);

        return list.isEmpty()
                ? ResponseEntity.noContent().build()   // 204
                : ResponseEntity.ok(list);             // 200
    }

    @PostMapping("/save")
    ResponseEntity<Void> saveContent(@RequestBody ContentRequest request) {
        contentService.saveContent(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/savelist")
    public ResponseEntity<?> saveList(@RequestBody List<ContentRequest> requests) {
        requests.forEach(contentService::saveContent);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/placedetail")
    public ResponseEntity<PlaceDetailResponse> getPlaceDetail(@RequestParam int contentId, @AuthenticationPrincipal String principal){

        String userId = null;
        if (StringUtils.hasText(principal) && NumberUtils.isCreatable(principal)) {
            userId = principal;
        }

        PlaceDetailResponse placeDetailResponse = contentService.getPlaceDetail(contentId,userId);
        return ResponseEntity.ok(placeDetailResponse);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<LastViewHistoryResponse>> getRecentViews(@AuthenticationPrincipal String userId) {
        List<LastViewHistoryResponse> recent = contentService.getRecentViews(userId);
        return ResponseEntity.ok().body(recent);
    }

}
