package com.swyp.catsgotogedog.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${allowed.origins.url}")
	private String allowedOriginsUrl;

	@Value("${allowed.http.methods}")
	private String allowedHttpMethods;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		String[] origins = allowedOriginsUrl.split(",");
		String[] methods = allowedHttpMethods.split(",");
		registry.addMapping("/**")
			.allowedOrigins(origins)
			.allowedMethods(methods)
			.allowedMethods("*")
			.allowCredentials(true)
			.maxAge(3600);
	}
}
