package com.swyp.catsgotogedog.common.security.service;



import com.swyp.catsgotogedog.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String providerId;
        String email = null;
        String name;

        log.info("RegistrationId: {}", registrationId);
        log.info("OAuth2 attributes: {}", oAuth2User.getAttributes());

        switch (registrationId) {
            case "naver":
                providerId = oAuth2User.getAttribute("id");
                name = oAuth2User.getAttribute("profile_nickname");
                //email = oAuth2User.getAttribute("email");
                break;
            case "kakao":
                providerId = oAuth2User.getAttribute("id");
                name = oAuth2User.getAttribute("nickname");
                //email = oAuth2User.getAttribute("kakao_account_email");
                break;
            case "google":
                providerId = oAuth2User.getAttribute("sub");
                name = oAuth2User.getAttribute("name");
                email = oAuth2User.getAttribute("email");
        }

        return super.loadUser(userRequest);
    }
}
