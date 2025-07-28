package com.swyp.catsgotogedog.content.service;

import com.swyp.catsgotogedog.content.domain.entity.Content;
import com.swyp.catsgotogedog.content.domain.entity.ContentDocument;
import com.swyp.catsgotogedog.content.domain.request.ContentRequest;
import com.swyp.catsgotogedog.content.repository.ContentElasticRepository;
import com.swyp.catsgotogedog.content.repository.ContentRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentService {
    private final ContentRepository contentRepository;
    private final ContentElasticRepository contentElasticRepository;

    public void saveContent(ContentRequest request){
        Content content = Content.builder()
                .categoryId(request.getCategoryId())
                .regionId(request.getRegionId())
                .addr1(request.getAddr1())
                .addr2(request.getAddr2())
                .image(request.getImage())
                .thumbImage(request.getThumbImage())
                .copyright(request.getCopyright())
                .mapx(request.getMapx())
                .mapy(request.getMapy())
                .mlevel(request.getMlevel())
                .tel(request.getTel())
                .title(request.getTitle())
                .zipcode(request.getZipcode())
                .contentTypeId(request.getContentTypeId())
                .build();
        contentRepository.save(content);
        contentElasticRepository.save(ContentDocument.from(content));
    }
}
