package com.swyp.catsgotogedog.User.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Getter
public class OAuth2WithdrawalConfig {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

//    @Value("${spring.security.oauth2.client.provider.kakao.admin-key}")
//    private String kakaoAdminKey;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    // Google Identity Platform 설정
    public static final String GOOGLE_REVOKE_URL = "https://oauth2.googleapis.com/revoke";

    // Kakao OAuth2 설정
    public static final String KAKAO_UNLINK_URL = "https://kapi.kakao.com/v1/user/unlink";

    // Naver OAuth2 설정
    public static final String NAVER_DELETE_URL = "https://nid.naver.com/oauth2.0/token";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
