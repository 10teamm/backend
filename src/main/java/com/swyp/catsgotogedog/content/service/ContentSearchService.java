package com.swyp.catsgotogedog.content.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import com.swyp.catsgotogedog.content.domain.entity.Content;
import com.swyp.catsgotogedog.content.domain.entity.ContentDocument;
import com.swyp.catsgotogedog.content.domain.response.ContentResponse;
import com.swyp.catsgotogedog.content.repository.ContentElasticRepository;
import com.swyp.catsgotogedog.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
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
    private final ElasticsearchOperations elasticsearchOperations;

    public List<ContentDocument> searchByKeyword(String keyword){
        return contentElasticRepository.findByTitleContaining(keyword);
    }


    public List<ContentResponse> search(String title,
                                        String addr1,
                                        String addr2,
                                        Integer contentTypeId) {

        boolean noTitle  = (title == null  || title.isBlank());
        boolean noAddr1  = (addr1 == null  || addr1.isBlank());
        boolean noAddr2  = (addr2 == null  || addr2.isBlank());
        boolean noTypeId = (contentTypeId == null || contentTypeId <= 0);

        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();

        if (noTitle && noAddr1 && noAddr2 && noTypeId) {
            boolBuilder.must(m -> m.matchAll(ma -> ma));
        } else {
            if (!noTitle) {
                boolBuilder.must(m -> m.matchPhrasePrefix(mp -> mp
                        .field("title")
                        .query(title)));
            } else {
                boolBuilder.must(m -> m.matchAll(ma -> ma));
            }

            if (!noAddr1) {
                boolBuilder.filter(f -> f.term(t -> t.field("addr1")
                        .value(addr1)));
            }

            if (!noAddr2) {
                boolBuilder.filter(f -> f.term(t -> t.field("addr2")
                        .value(addr2)));
            }

            if (!noTypeId) {
                boolBuilder.filter(f -> f.term(t -> t.field("contentTypeId")
                        .value(contentTypeId)));
            }
        }

        Query esQuery = new Query.Builder()
                .bool(boolBuilder.build())
                .build();

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(esQuery)
                .withPageable(PageRequest.of(0, 20))
                .build();

        List<Integer> ids = elasticsearchOperations.search(nativeQuery, ContentDocument.class).stream()
                .map(SearchHit::getContent)
                .map(ContentDocument::getContentId)
                .toList();

        if (ids.isEmpty()) return List.of();

        Map<Integer, Content> map = contentRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Content::getContentId, c -> c));

        return ids.stream()
                .map(map::get)
                .filter(Objects::nonNull)
                .map(ContentResponse::from)
                .toList();
    }

}
