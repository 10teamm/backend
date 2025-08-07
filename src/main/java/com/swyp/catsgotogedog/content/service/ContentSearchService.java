package com.swyp.catsgotogedog.content.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import com.swyp.catsgotogedog.User.domain.entity.User;
import com.swyp.catsgotogedog.User.repository.UserRepository;
import com.swyp.catsgotogedog.content.domain.entity.Content;
import com.swyp.catsgotogedog.content.domain.entity.ContentDocument;
import com.swyp.catsgotogedog.content.domain.entity.ContentImage;
import com.swyp.catsgotogedog.content.domain.entity.RegionCode;
import com.swyp.catsgotogedog.content.domain.response.ContentResponse;
import com.swyp.catsgotogedog.content.domain.response.RegionCodeResponse;
import com.swyp.catsgotogedog.content.repository.*;
import com.swyp.catsgotogedog.review.repository.ContentReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentSearchService {
    private final ContentRepository contentRepository;
    private final ContentElasticRepository contentElasticRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ContentReviewRepository contentReviewRepository;
    private final ContentWishRepository contentWishRepository;
    private final UserRepository userRepository;
    private final RegionCodeRepository regionCodeRepository;
    private final SightsInformationRepository sightsInformationRepository;
    private final RestaurantInformationRepository restaurantInformationRepository;
    private final HashtagRepository hashtagRepository;
    private final ViewTotalRepository viewTotalRepository;

    public List<ContentDocument> searchByKeyword(String keyword){
        return contentElasticRepository.findByTitleContaining(keyword);
    }


    public List<ContentResponse> search(String title,
                                        String sidoCode,
                                        String sigunguCode,
                                        Integer contentTypeId,
                                        String userId) {

        if (userId != null) {
            Optional<User> user = userRepository.findById(Integer.parseInt(userId));
        }

        boolean noTitle  = (title == null  || title.isBlank());
        boolean noSidoCode  = (sidoCode == null  || sidoCode.isBlank());
        boolean noSigunguCode = (sigunguCode == null  || sigunguCode.isBlank());
        boolean noTypeId = (contentTypeId == null || contentTypeId <= 0);

        System.out.println("noTypeId : "+noTypeId);
        System.out.println("contentTypeId : "+contentTypeId);

        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();

        if (noTitle && noSidoCode && noSigunguCode && noTypeId) {
            boolBuilder.must(m -> m.matchAll(ma -> ma));
        } else {
            if (!noTitle) {
                boolBuilder.must(m -> m.matchPhrasePrefix(mp -> mp
                        .field("title")
                        .query(title)));
            } else {
                boolBuilder.must(m -> m.matchAll(ma -> ma));
            }

            if (!noSidoCode) {
                boolBuilder.filter(f -> f.term(t -> t.field("sido_code")
                        .value(sidoCode)));
            }

            if (!noSigunguCode) {
                boolBuilder.filter(f -> f.term(t -> t.field("sigungu_code")
                        .value(sigunguCode)));
            }

            if (!noTypeId) {
                boolBuilder.filter(f -> f.term(t -> t.field("content_type_id")
                        .value(contentTypeId)));
            }
        }

        Query esQuery = new Query.Builder()
                .bool(boolBuilder.build())
                .build();

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(esQuery)
                .withPageable(Pageable.unpaged())
                .build();

        List<Integer> ids = elasticsearchOperations
                .search(nativeQuery, ContentDocument.class).stream()
                .map(SearchHit::getContent)
                .map(ContentDocument::getContentId)
                .toList();

        if (ids.isEmpty()) return List.of();

        Map<Integer, Content> contentMap = contentRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Content::getContentId, c -> c));

        return ids.stream()
                .map(contentMap::get)
                .filter(Objects::nonNull)
                .filter(c -> c.getSidoCode() != 0 && c.getSigunguCode() != 0)
                .map(content -> {

                    int id = content.getContentId();
                    double avg = getAverageScore(id);
                    boolean wishData = (userId != null) ? getWishData(userId, id) : false;
                    RegionCodeResponse regionName = getRegionName(content.getSidoCode(), content.getSigunguCode());
                    List<String> hashtag = hashtagRepository.findContentsByContentId(id);
                    String restDate = getRestDate(id);
                    int totalView = viewTotalRepository.findTotalViewByContentId(id).orElse(0);
                    int wishCnt = contentWishRepository.countByContent_ContentId(id);

                    return ContentResponse.from(
                            content, avg, wishData, regionName, hashtag, restDate, totalView, wishCnt
                    );
                })
                .toList();

    }

    public double getAverageScore(int contentId) {
        Double avg = contentReviewRepository.findAvgScoreByContentId(contentId);
        double value = (avg != null) ? avg : 0.0;
        return Math.round(value * 10.0) / 10.0;
    }

    public Boolean getWishData(String userId, int contentId){
        var existing = contentWishRepository.findByUserIdAndContentId(Integer.parseInt(userId), contentId);

        boolean liked;

        liked = existing.isPresent();

        return liked;
    }

    public RegionCodeResponse getRegionName(int sidoCode, int sigunguCode){
        RegionCode sido = regionCodeRepository.findBySidoCodeAndRegionLevel(sidoCode,1);

        RegionCode sigungu = regionCodeRepository.findByParentCodeAndSigunguCodeAndRegionLevel(sidoCode, sigunguCode,2);

        String sidoName = sido.getRegionName();
        String sigunguName = sigungu.getRegionName();

        return new RegionCodeResponse(sidoName,sigunguName);
    }

    public String getRestDate(int contentId) {

        String restDate = sightsInformationRepository.findRestDateByContentId(contentId);
        if (restDate != null) {
            return restDate;
        }

        restDate = restaurantInformationRepository.findRestDateByContentId(contentId);
        return restDate;
    }


}
