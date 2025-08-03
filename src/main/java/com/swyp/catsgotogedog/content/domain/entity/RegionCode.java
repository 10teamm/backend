package com.swyp.catsgotogedog.content.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "region_code")
@Builder
public class RegionCode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "region_id")
	private int regionId;

	@Column(name = "region_name", nullable = false)
	private String regionName;

	@Column(name = "sido_code")
	private int sidoCode;

	@Column(name = "sigungu_code")
	private int sigunguCode;

	@Column(name = "parent_code")
	private int parentCode;

	@Column(name = "region_level")
	private int regionLevel;
}
