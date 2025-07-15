package com.swyp.catsgotogedog.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.milvus.client.MilvusClient;
import io.milvus.client.MilvusServiceClient;
import io.milvus.param.ConnectParam;
import io.milvus.param.ConnectParam.Builder;

@Configuration
public class MilvusConfig {

	@Value("${milvus.host}")
	private String milvusHost;

	@Value("${milvus.port}")
	private int milvusPort;

	 @Value("${milvus.username}")
	 private String milvusUsername;

	 @Value("${milvus.password}")
	 private String milvusPassword;

	@Bean
	public MilvusClient milvusClient() {
		Builder connectParamBuilder = ConnectParam.newBuilder()
			.withHost(milvusHost)
			.withPort(milvusPort);

		if (milvusUsername != null && milvusPassword != null) {
			connectParamBuilder.withAuthorization(milvusUsername, milvusPassword);
		}

		return new MilvusServiceClient(connectParamBuilder.build());
	}
}
