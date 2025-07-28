package com.swyp.catsgotogedog.content.service;

import com.swyp.catsgotogedog.content.domain.entity.ContentDocument;
import com.swyp.catsgotogedog.content.repository.ContentElasticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentSearchService {
    private final ContentElasticRepository contentElasticRepository;

    public List<ContentDocument> searchByKeyword(String keyword){
        return contentElasticRepository.findByTitleContaining(keyword);
    }

}
