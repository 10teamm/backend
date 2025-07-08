package com.swyp.catsgotogedog.common.security.service;


import com.swyp.catsgotogedog.User.domain.entity.User;
import com.swyp.catsgotogedog.User.domain.entity.UserRole;
import com.swyp.catsgotogedog.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String loginId = provider + "_" + providerId;

        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        User user;
        if (optionalUser.isEmpty()) {
            user = User.builder()
                    .loginId(loginId)
                    .name(oAuth2User.getAttribute("name"))
                    .provider(provider)
                    .providerId(providerId)
                    .role(UserRole.USER)
                    .build();
            userRepository.save(user);
        } else {
            user = optionalUser.get();
        }
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
