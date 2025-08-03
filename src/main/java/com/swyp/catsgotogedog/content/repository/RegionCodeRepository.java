package com.swyp.catsgotogedog.content.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.catsgotogedog.content.domain.entity.RegionCode;

public interface RegionCodeRepository extends JpaRepository<RegionCode, Integer> {
	Optional<RegionCode> findBySidoCodeAndSigunguCode(int sidoCode, int sigunguCode);
	Optional<RegionCode> findBySidoCodeAndSigunguCodeIsNull(int sidoCode);
	List<RegionCode> findBySidoCode(int sidoCode);
  
  RegionCode findRegionNameBySidoCodeAndRegionLevel(int sidoCode, int regionLevel);

  RegionCode findRegionNameByParentCodeAndSigunguCodeAndRegionLevel(int parentCode, int sigunguCode, int regionLevel);
}
