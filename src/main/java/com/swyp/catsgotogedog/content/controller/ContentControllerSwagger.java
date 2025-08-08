package com.swyp.catsgotogedog.content.controller;

import com.swyp.catsgotogedog.content.domain.response.ContentResponse;
import com.swyp.catsgotogedog.content.domain.response.LastViewHistoryResponse;
import com.swyp.catsgotogedog.content.domain.response.PlaceDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Tag(name = "Content", description = "컨텐츠 (관광지, 숙소, 음식점, 축제/공연/행사) 관련 API")
public interface ContentControllerSwagger {

    @Operation(
            summary = "컨텐츠 검색",
            description = "제목, 시/도, 시/군/구, 컨텐츠 유형으로 장소를 검색합니다. "
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContentResponse.class)))),
            @ApiResponse(responseCode = "204", description = "검색 결과 없음"),
            @ApiResponse(responseCode = "400", description = "요청 파라미터가 유효하지 않음")
    })
    ResponseEntity<List<ContentResponse>> search(
            @Parameter(description = "장소 검색어", required = false)
            @RequestParam(required = false) String title,

            @Parameter(description = "시/도 코드", required = false)
            @RequestParam(required = false) String sido,

            @Parameter(description = "시/군/구 코드", required = false)
            @RequestParam(required = false) String sigungu,

            @Parameter(description = "컨텐츠 유형 ID", required = false)
            @RequestParam(required = false) Integer contentTypeId,

            @Parameter(hidden = true)
            @AuthenticationPrincipal String principal
    )throws IOException;

    @Operation(
            summary = "공간 상세 조회",
            description = "contentId로 장소 상세 정보를 조회"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = PlaceDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 contentId에 대한 데이터 없음")
    })
    ResponseEntity<PlaceDetailResponse> getPlaceDetail(
            @Parameter(description = "조회할 컨텐츠 ID", required = true)
            @RequestParam int contentId,

            @Parameter(hidden = true)
            @AuthenticationPrincipal String principal
    );
  
    @Operation(
            summary = "찜 체크 기능",
            description = "로그인된 사용자의 해당 콘텐츠 찜 상태를 설정 또는 해제. " +
                    "이미 찜돼 있으면 해제(false), 아니면 찜(true). " +
                    "비회원이거나 인증 정보가 없으면 false 반환",
            security = { @SecurityRequirement(name = "bearer-key") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "찜 처리 결과 반환",
                    content = @Content(schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "404", description = "해당 사용자 또는 콘텐츠 없음")
    })
    @PostMapping("/wish-check")
    ResponseEntity<?> checkWish(
            @Parameter(hidden = true)
            @AuthenticationPrincipal String userId,

            @Parameter(description = "컨텐츠 ID", required = true)
            @RequestParam int contentId
    );
  
    @Operation(
            summary = "방문 여부 체크",
            description = "로그인된 사용자의 해당 콘텐츠 방문 여부를 체크 또는 해제, " +
                    "체크돼 있으면 해제하고(false), 체크돼 있지 않으면 체크합니다(true). " +
                    "비회원인 경우 아무 동작 없이 false 반환",
            security = { @SecurityRequirement(name = "bearer-key") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "체크 결과",
                    content = @Content(schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "404", description = "해당 콘텐츠 또는 사용자 없음")
    })
    @GetMapping("/visited-check")
    ResponseEntity<?> checkVisited(
            @Parameter(hidden = true)
            @AuthenticationPrincipal String userId,

            @Parameter(description = "장소 ID", required = true)
            @RequestParam int contentId
    );

}
