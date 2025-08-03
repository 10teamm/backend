package com.swyp.catsgotogedog.content.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class RegionCode {
    @Id
    private Integer regionId;

    private String  regionName;
    private Integer sidoCode;
    private Integer sigunguCode;
    private Integer parentCode;
    private Short   regionLevel;
}
