package com.swyp.catsgotogedog.User.domain.request;


import lombok.Data;

@Data
public class GoogleOAuthRequest {

    private String idToken;
    private Account account;
    private Profile profile;
    private OAuthProfile OAuthProfile;

    @Data
    public static class Account {
        private String provider;
        private String type;
        private String providerAccountId;
        private String access_token;
        private long expires_at;
        private String scope;
        private String token_type;
        private String id_token;  // 여기서 id_token이 실제 JWT 토큰입니다.
    }

    @Data
    public static class Profile {
        private String id;
        private String name;
        private String email;
        private String image;
    }

    @Data
    public static class OAuthProfile {
        private String iss;
        private String azp;
        private String aud;
        private String sub;
        private String hd;
        private String email;
        private boolean email_verified;
        private String at_hash;
        private String name;
        private String picture;
        private String given_name;
        private String family_name;
        private long iat;
        private long exp;
    }
}
