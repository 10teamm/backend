package com.swyp.catsgotogedog.content.service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import com.swyp.catsgotogedog.User.domain.entity.User;
import com.swyp.catsgotogedog.User.repository.UserRepository;
import com.swyp.catsgotogedog.content.domain.entity.*;
import com.swyp.catsgotogedog.content.domain.response.ContentResponse;
import com.swyp.catsgotogedog.content.domain.response.RegionCodeResponse;
import com.swyp.catsgotogedog.content.repository.*;
import com.swyp.catsgotogedog.content.repository.projection.RestDateProjection;
import com.swyp.catsgotogedog.content.repository.projection.ViewTotalProjection;
import com.swyp.catsgotogedog.content.repository.projection.WishCountProjection;
import com.swyp.catsgotogedog.review.repository.ContentReviewRepository;
import com.swyp.catsgotogedog.review.repository.projection.AvgScoreProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
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
                                        List<String> sigunguCode,
                                        Integer contentTypeId,
                                        String userId) {

        if (userId != null) {
            Optional<User> user = userRepository.findById(Integer.parseInt(userId));
        }

        boolean noTitle  = (title == null  || title.isBlank());
        boolean noSidoCode  = (sidoCode == null  || sidoCode.isBlank());
        boolean noSigunguCode = (sigunguCode == null  || sigunguCode.isEmpty());
        boolean noTypeId = (contentTypeId == null || contentTypeId <= 0);

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
                boolBuilder.filter(f -> f.terms(t -> t
                        .field("sigungu_code")
                        .terms(v -> v.value(
                                sigunguCode.stream()
                                        .map(FieldValue::of)
                                        .toList()
                        ))
                ));
            }

            if (!noTypeId) {
                boolBuilder.filter(f -> f.term(t -> t.field("content_type_id")
                        .value(contentTypeId)));
            }
        }

        Query baseQuery = new Query.Builder()
                .bool(boolBuilder.build())
                .build();

        long seed = dailySeed(contentTypeId);

        Query esQuery = new Query.Builder()
                .functionScore(fs -> fs
                        .query(baseQuery)
                        .functions(
                                FunctionScore.of(fn -> fn
                                        .randomScore(rs -> rs
                                                .seed(String.valueOf(seed))
                                                .field("content_id")
                                        )
                                )
                        )
                        .boostMode(FunctionBoostMode.Replace)
                        .scoreMode(FunctionScoreMode.Sum)
                )
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

        Map<Integer, Double> avgScoreMap = new HashMap<>();
        List<AvgScoreProjection> avgRows = contentReviewRepository.findAvgScoreByContentIdIn(ids);
        for (AvgScoreProjection row : avgRows) {
            Double v = row.getAvgScore();
            avgScoreMap.put(row.getContentId(),v);
        }

        Set<Integer> wishedSet;
        if (userId != null && !userId.isBlank()) {
            wishedSet = contentWishRepository
                    .findWishedContentIdsByUserIdAndContentIds(Integer.parseInt(userId), ids);
        } else {
            wishedSet = Collections.emptySet();
        }

        Map<Integer, List<String>> hashtagMap = new HashMap<>();
        List<Hashtag> hashtags = hashtagRepository.findByContentIdIn(ids);
        for (Hashtag h : hashtags) {
            int cid = h.getContentId();
            List<String> list = hashtagMap.get(cid);
            if (list == null) {
                list = new ArrayList<>();
                hashtagMap.put(cid, list);
            }
            list.add(h.getContent());
        }

        Map<Integer, String> restDateMap = new HashMap<>();
        List<RestDateProjection> sightRows = sightsInformationRepository.findRestDateByContentIdIn(ids);
        for (RestDateProjection r : sightRows) {
            String rd = r.getRestDate();
            if (rd != null) {
                restDateMap.put(r.getContentId(), rd);
            }
        }
        List<RestDateProjection> restRows = restaurantInformationRepository.findRestDateByContentIdIn(ids);
        for (RestDateProjection r : restRows) {
            String rd = r.getRestDate();
            if (rd != null) {
                restDateMap.putIfAbsent(r.getContentId(), rd);
            }
        }

        Map<Integer, Integer> totalViewMap = new HashMap<>();
        List<ViewTotalProjection> viewRows = viewTotalRepository.findTotalViewByContentIdIn(ids);
        for (ViewTotalProjection v : viewRows) {
            int tv = v.getTotalView();
            totalViewMap.put(v.getContentId(), tv);
        }

        Map<Integer, Integer> wishCntMap = new HashMap<>();
        List<WishCountProjection> wishCntRows = contentWishRepository.countByContentIdIn(ids);
        for (WishCountProjection w : wishCntRows) {
            int cnt = w.getWishCount();
            wishCntMap.put(w.getContentId(), cnt);
        }

        return ids.stream()
                .map(contentMap::get)
                .filter(Objects::nonNull)
                .filter(c -> c.getSidoCode() != 0 && c.getSigunguCode() != 0)
                .map(content -> {
                    int id = content.getContentId();

                    double avg = avgScoreMap.getOrDefault(id, 0.0);
                    boolean wishData = wishedSet.contains(id);
                    List<String> hashtag = hashtagMap.getOrDefault(id, List.of());
                    String restDate = restDateMap.get(id);
                    int totalView = totalViewMap.getOrDefault(id, 0);
                    int wishCnt = wishCntMap.getOrDefault(id, 0);

                    RegionCodeResponse regionName = getRegionName(content.getSidoCode(), content.getSigunguCode());

                    return ContentResponse.from(content, avg, wishData, regionName, hashtag, restDate, totalView, wishCnt);
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

    private long dailySeed(Integer contentTypeId) {
        long base = LocalDate.now(ZoneId.of("Asia/Seoul")).toEpochDay();
        long cat  = (contentTypeId != null && contentTypeId > 0) ? contentTypeId : 0;
        return base * 1_000_000 + cat;
    }
}
