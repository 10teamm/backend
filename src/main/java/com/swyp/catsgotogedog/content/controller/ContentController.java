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
    public ResponseEntity<List<ContentResponse>> search(@RequestParam String keyword){
        return ResponseEntity.ok(contentSearchService.searchByTitle(keyword));
    }

    @PostMapping("/save")
    ResponseEntity<Void> saveContent(@RequestBody ContentRequest request) {
        contentService.saveContent(request);
        return ResponseEntity.ok().build();
    }
}
