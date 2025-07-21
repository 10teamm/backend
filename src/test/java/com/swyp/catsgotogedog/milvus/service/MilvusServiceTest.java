package com.swyp.catsgotogedog.milvus.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.swyp.catsgotogedog.common.milvus.service.MilvusService;

import io.milvus.client.MilvusClient;
import io.milvus.param.collection.LoadCollectionParam;

@ExtendWith(MockitoExtension.class)
public class MilvusServiceTest {

	@Mock
	private MilvusClient milvusClient;

	@InjectMocks
	private MilvusService milvusService;

	private static final String TEST_COLLECTION_NAME = "catsgotogedog_test_collection";

	@BeforeEach
	void setUp() throws Exception {
		ReflectionTestUtils.setField(milvusService, "collectionName", TEST_COLLECTION_NAME);
	}

	@Test
	@DisplayName("컬렉션 로드 테스트 (성공)")
	void loadCollection_success() {
		assertDoesNotThrow(() -> milvusService.loadCollection());

		verify(milvusClient, times(1)).loadCollection(any(LoadCollectionParam.class));
	}

	@Test
	@DisplayName("컬렉션 로드 테스트 (실패)")
	void loadCollection_Fail() {
		doThrow(new RuntimeException("Milvus client load 실패")).when(milvusClient).loadCollection(any(LoadCollectionParam.class));

		RuntimeException thrown = assertThrows(RuntimeException.class, () -> milvusService.loadCollection());

		assert(thrown.getMessage().equals("Milvus user Creation Failed"));

		verify(milvusClient, times(1)).loadCollection(any(LoadCollectionParam.class));
	}
}
