package com.swyp.catsgotogedog.content.repository;

import com.swyp.catsgotogedog.content.domain.entity.RegionCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionCodeRepository extends JpaRepository<RegionCode, Integer> {
    RegionCode findRegionNameBySidoCodeAndRegionLevel(int sidoCode, int regionLevel);

    RegionCode findRegionNameByParentCodeAndSigunguCodeAndRegionLevel(int parentCode, int sigunguCode, int regionLevel);
}
