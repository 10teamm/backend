package com.swyp.catsgotogedog.content.service;

import com.swyp.catsgotogedog.content.domain.entity.Content;
import com.swyp.catsgotogedog.content.domain.entity.ContentDocument;
import com.swyp.catsgotogedog.content.domain.response.ContentResponse;
import com.swyp.catsgotogedog.content.repository.ContentElasticRepository;
import com.swyp.catsgotogedog.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentSearchService {
    private final ContentRepository contentRepository;
    private final ContentElasticRepository contentElasticRepository;

    public List<ContentDocument> searchByKeyword(String keyword){
        return contentElasticRepository.findByTitleContaining(keyword);
    }

    public List<ContentResponse> searchByTitle(String keyword) {

        List<Integer> ids = contentElasticRepository.findByTitleContaining(keyword)
                .stream()
                .map(ContentDocument::getContentId)
                .toList();

        if (ids.isEmpty()) return List.of();

        List<Content> contents = contentRepository.findAllById(ids);

        Map<Integer, Content> map = contents.stream()
                .collect(Collectors.toMap(Content::getContentId, c -> c));

        return ids.stream()
                .map(map::get)
                .filter(Objects::nonNull)
                .map(ContentResponse::from)
                .toList();
    }

}
