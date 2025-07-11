package com.swyp.catsgotogedog.milvus.service;

import org.springframework.stereotype.Service;

import io.milvus.client.MilvusClient;
import io.milvus.param.collection.CreateDatabaseParam;
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
	private static final String COLLECTION_NAME = "catsgotogedog_collection";

	/**
	 * 컬렉션 로딩
	 * 스프링 시작시 milvus 컬렉션 로드
	 * 메모리에 올라가는 작업
	 */
	@PostConstruct
	public void loadCollection() {
		try {
			milvusClient.loadCollection(LoadCollectionParam.newBuilder()
				.withCollectionName(COLLECTION_NAME)
			 	.build());
			 log.info("Collection 로드 완료 :: {}", COLLECTION_NAME);
		} catch (Exception e) {
			throw new RuntimeException("Milvus user Creation Failed");
		}
	}
}
