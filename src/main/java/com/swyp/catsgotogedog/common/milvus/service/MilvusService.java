package com.swyp.catsgotogedog.common.milvus.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.milvus.client.MilvusClient;
import io.milvus.grpc.GetCollectionStatisticsResponse;
import io.milvus.param.R;
import io.milvus.param.collection.CreateDatabaseParam;
import io.milvus.response.GetCollStatResponseWrapper;
import io.milvus.grpc.GetCollectionStatisticsResponse;
import io.milvus.param.collection.GetCollectionStatisticsParam;
import io.milvus.param.collection.LoadCollectionParam;
import io.milvus.response.SearchResultsWrapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MilvusService {

	private final MilvusClient milvusClient;

	/**
	 * TODO : 개발, 운영 milvus 컬렉션을 나누어야 할 필요가 있어보임 우선 하나의 컬렉션을 사용
	 */
	@Value("${milvus.collection-name}")
	private String collectionName;

	/**
	 * 컬렉션 로딩
	 * 스프링 시작시 milvus 컬렉션 로드
	 * 메모리에 올라가는 작업
	 */
	@PostConstruct
	public void loadCollection() {
		try {
			milvusClient.loadCollection(LoadCollectionParam.newBuilder()
				.withCollectionName(collectionName)
			 	.build());
			 log.info("Collection 로드 완료 :: {}", collectionName);
		} catch (Exception e) {
			throw new RuntimeException("Milvus user Creation Failed");
		}
	}
}
