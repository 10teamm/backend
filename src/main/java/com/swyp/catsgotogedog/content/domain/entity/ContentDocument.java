package com.swyp.catsgotogedog.content.domain.entity;

import org.springframework.data.annotation.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@Document(indexName = "content", createIndex = true)
@Setting(settingPath = "elasticsearch/search-setting.json")
@Mapping(mappingPath = "elasticsearch/search-mapping.json")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ContentDocument {

    @Id
    @Field(type= FieldType.Integer)
    private int contentId;

    private int categoryId;

    private String addr1;

    private String addr2;

    private String title;

    private int contentTypeId;

    public static ContentDocument from(Content content){
        return ContentDocument.builder()
                .contentId(content.getContentId())
                .categoryId(content.getCategoryId())
                .addr1(content.getAddr1())
                .addr2(content.getAddr2())
                .title(content.getTitle())
                .contentTypeId(content.getContentTypeId())
                .build();
    }

}
