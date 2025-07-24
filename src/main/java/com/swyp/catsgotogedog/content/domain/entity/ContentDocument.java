package com.swyp.catsgotogedog.content.domain.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@Document(indexName = "content", createIndex = true)
@Setting(settingPath = "elasticsearch/search-setting.json")
@Mapping(mappingPath = "elasticsearch/search-mapping.json")
public class ContentDocument {

    @Id
    @Field(type= FieldType.Integer)
    private int content_id;


}
