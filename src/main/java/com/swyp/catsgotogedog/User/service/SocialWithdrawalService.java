package com.swyp.catsgotogedog.User.service;

import com.swyp.catsgotogedog.User.config.OAuth2WithdrawalConfig;
import com.swyp.catsgotogedog.User.domain.entity.User;
import com.swyp.catsgotogedog.global.exception.CatsgotogedogException;
import com.swyp.catsgotogedog.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialWithdrawalService {

    private final OAuth2WithdrawalConfig oAuth2Config;
    private final RestTemplate restTemplate;

    /**
     * 소셜 서비스에서 회원 탈퇴 처리
     */
    public void withdrawFromSocialProvider(User user) {
        String provider = user.getProvider();
        String providerId = user.getProviderId();
        String token = user.getOauthToken();

        switch (provider.toLowerCase()) {
            case "google" -> withdrawFromGoogle(providerId,token);
            case "kakao" -> withdrawFromKakao(providerId, token);
            case "naver" -> withdrawFromNaver(providerId, token);
            default -> {
                log.warn("지원하지 않는 소셜 제공자입니다: {}", provider);
                throw new CatsgotogedogException(ErrorCode.UNSUPPORTED_SOCIAL_PROVIDER);
            }
        }
    }

    /**
     * Google 계정 연결 해제
     * access_token을 사용하여 revoke
     */
    private void withdrawFromGoogle(String providerId, String token) {
        try {
            log.debug("Google OAuth2 연결 해제 시도 - ProviderId: {}", providerId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String url = OAuth2WithdrawalConfig.GOOGLE_REVOKE_URL + "?token=" + token;

            HttpEntity<String> request = new HttpEntity<>("", headers);

            log.debug("Google revoke API 호출 - URL: {}", OAuth2WithdrawalConfig.GOOGLE_REVOKE_URL);
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.debug("Google 연결 해제 성공 - ProviderId: {}", providerId);
            } else {
                log.warn("Google 연결 해제 응답 상태: {} - ProviderId: {}",
                        response.getStatusCode(), providerId);
                throw new CatsgotogedogException(ErrorCode.SOCIAL_WITHDRAWAL_FAILED);
            }
        } catch (Exception e) {
            log.error("Google 연결 해제 실패 - ProviderId: {}, Error Type: {}, Message: {}",
                    providerId, e.getClass().getSimpleName(), e.getMessage());
            throw new CatsgotogedogException(ErrorCode.SOCIAL_WITHDRAWAL_FAILED);
        }
    }

    /**
     * Kakao 계정 연결 해제
     */
    private void withdrawFromKakao(String providerId, String token) {
        try {

            // 어드민 키 방식
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Authorization", "KakaoAK " + oAuth2Config.getKakaoAdminKey());
//            headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
//
//            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//            params.add("target_id_type", "user_id");
//            params.add("target_id", providerId);
//            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            // Bearer Token 방식
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            HttpEntity<String> request = new HttpEntity<>("", headers);

            ResponseEntity<String> response = restTemplate.exchange(
                OAuth2WithdrawalConfig.KAKAO_UNLINK_URL, HttpMethod.POST, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.debug("Kakao 연결 해제 성공 - ProviderId: {} - Body: {}", providerId, response.getBody());
            } else {
                log.warn("Kakao 연결 해제 응답 상태: {} - {}", response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            log.error("Kakao 연결 해제 실패 : Error: {}", e.getMessage());
            throw new CatsgotogedogException(ErrorCode.SOCIAL_WITHDRAWAL_FAILED);
        }
    }

    /**
     * Naver 계정 연결 해제
     */
    private void withdrawFromNaver(String providerId, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "delete");
            params.add("client_id", oAuth2Config.getNaverClientId());
            params.add("client_secret", oAuth2Config.getNaverClientSecret());
            params.add("access_token", token);
            params.add("service_provider", "NAVER");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                OAuth2WithdrawalConfig.NAVER_DELETE_URL, HttpMethod.POST, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.debug("Naver 연결 해제 성공 - ProviderId: {} - Body: {}", providerId, response.getBody());
            } else {
                log.warn("Naver 연결 해제 응답 상태: {} - ProviderId: {}",
                        response.getStatusCode(), providerId);
            }
        } catch (Exception e) {
            log.error("Naver 연결 해제 실패 - ProviderId: {}, Error: {}", providerId, e.getMessage());
            throw new CatsgotogedogException(ErrorCode.SOCIAL_WITHDRAWAL_FAILED);
        }
    }
}
