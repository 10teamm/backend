package com.swyp.catsgotogedog.content.controller;

import com.swyp.catsgotogedog.content.domain.response.ContentResponse;
import com.swyp.catsgotogedog.global.CatsgotogedogApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import java.io.IOException;

import java.util.List;

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
}
