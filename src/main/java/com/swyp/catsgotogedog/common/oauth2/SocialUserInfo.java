package com.swyp.catsgotogedog.common.oauth2;

import java.util.Map;

public record SocialUserInfo(String id, String email, String name) {

    public static SocialUserInfo of(String provider, Map<String, Object> attr) {
        return switch (provider) {
            case "google" -> new SocialUserInfo(
                    (String) attr.get("sub"),
                    (String) attr.get("email"),
                    (String) attr.get("name")
            );
            case "kakao" -> {
                Map<String, Object> acc = (Map<String, Object>) attr.get("kakao_account");
                Map<String, Object> prof = (Map<String, Object>) acc.get("profile");
                yield new SocialUserInfo(
                        String.valueOf(attr.get("id")),
                        (String) acc.get("email"),
                        (String) prof.get("nickname")
                );
            }
            case "naver" -> {
                Map<String, Object> res = (Map<String, Object>) attr.get("response");
                yield new SocialUserInfo(
                        (String) res.get("id"),
                        (String) res.get("email"),
                        (String) res.get("name")
                );
            }
            default -> throw new IllegalArgumentException("지원하지 않는 provider");
        };
    }
}