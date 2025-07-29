package com.swyp.catsgotogedog.content.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.swyp.catsgotogedog.content.domain.entity.RegionCode;

public interface RegionCodeRepository extends JpaRepository<RegionCode, Long> {
	Optional<RegionCode> findBySidoCodeAndSigunguCode(int sidoCode, int sigunguCode);
	Optional<RegionCode> findBySidoCodeAndSigunguCodeIsNull(int sidoCode);
	List<RegionCode> findBySidoCode(int sidoCode);
}
