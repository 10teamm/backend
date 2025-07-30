package com.swyp.catsgotogedog.content.controller;

import com.swyp.catsgotogedog.content.domain.request.ContentRequest;
import com.swyp.catsgotogedog.content.domain.response.ContentResponse;
import com.swyp.catsgotogedog.content.service.ContentSearchService;
import com.swyp.catsgotogedog.content.service.ContentService;
import org.springframework.http.ResponseEntity;
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
            @RequestParam(required = false) String addr1,
            @RequestParam(required = false) String addr2,
            @RequestParam(required = false) Integer contentTypeId) {

        List<ContentResponse> list = contentSearchService.search(title, addr1, addr2, contentTypeId);

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

}
