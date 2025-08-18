package com.swyp.catsgotogedog.content.service;

import com.swyp.catsgotogedog.content.domain.entity.Content;
import com.swyp.catsgotogedog.content.domain.response.MissingImageResponse;
import com.swyp.catsgotogedog.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentMissingDataService {

    private final ContentRepository contentRepository;

    public List<MissingImageResponse> getMissingImage() {

        List<Content> contents = contentRepository.findByImageIsNullOrImage("");
        return contents.stream()
                .map(MissingImageResponse::from)
                .toList();
    }
}
