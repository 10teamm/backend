package com.swyp.catsgotogedog.common.security.service;



import com.swyp.catsgotogedog.User.domain.entity.User;
import com.swyp.catsgotogedog.User.repository.UserRepository;
import com.swyp.catsgotogedog.common.oauth2.SocialUserInfo;
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
    public OAuth2User loadUser(OAuth2UserRequest req) {
        OAuth2User oAuth2User = super.loadUser(req);

        String provider = req.getClientRegistration().getRegistrationId();   // google/kakao/naver
        SocialUserInfo info = SocialUserInfo.of(provider, oAuth2User.getAttributes());

        User user = userRepository.findByProviderAndProviderId(provider, info.id())
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .provider(provider)
                                .providerId(info.id())
                                .email(info.email())
                                .name(info.name())
                                .build()));

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
