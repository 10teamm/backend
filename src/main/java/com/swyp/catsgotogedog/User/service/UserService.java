package com.swyp.catsgotogedog.User.service;



import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.swyp.catsgotogedog.User.domain.entity.User;
import com.swyp.catsgotogedog.User.domain.entity.UserRole;
import com.swyp.catsgotogedog.User.domain.request.JoinRequest;
import com.swyp.catsgotogedog.User.domain.request.LoginRequest;
import com.swyp.catsgotogedog.User.domain.request.UserProfileUpdateRequest;
import com.swyp.catsgotogedog.User.domain.response.UserProfileResponse;
import com.swyp.catsgotogedog.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    public boolean checkLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    public void join(JoinRequest req) {
        userRepository.save(req.toEntity(encoder.encode(req.getPassword())));
    }

    public User login(LoginRequest req) {
        Optional<User> optionalUser = userRepository.findByLoginId(req.getLoginId());

        if(optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            return null;
        }

        return user;
    }

    public User getLoginUserByLoginId(String loginId) {
        if(loginId == null) return null;

        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }

    public void updateUserProfile(String loginId, UserProfileUpdateRequest req) {

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with loginId: " + loginId));


        user.setAge(req.getAge());
        user.setEducation(req.getEducation());
        user.setExperience(req.getExperience());
        user.setKoreanProficiency(req.getKoreanProficiency());
        user.setRegion(req.getRegion());
        user.setVisaDescription(req.getVisaDescription());

        userRepository.save(user);
    }

    public UserProfileResponse getUserProfile(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with loginId: " + loginId));

        String formattedBirth = null;
        if (user.getBirth() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            formattedBirth = sdf.format(user.getBirth());
        }

        return new UserProfileResponse(
                user.getLoginId(),
                user.getName(),
                formattedBirth,
                user.getPhoneNumber(),
                user.getEmail(),
                user.getAddress(),
                user.getVisaType(),
                user.getAge(),
                user.getEducation(),
                user.getExperience(),
                user.getKoreanProficiency(),
                user.getRegion(),
                user.getVisaDescription()
        );
    }

    public String processGoogleIdToken(String idToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(idToken);

            String kid = signedJWT.getHeader().getKeyID();
            if (kid == null) {
                throw new Exception("토큰에 'kid' 값이 없습니다.");
            }

            URL jwksUrl = new URL("https://www.googleapis.com/oauth2/v3/certs");
            JWKSet jwkSet = JWKSet.load(jwksUrl);

            JWK jwk = jwkSet.getKeyByKeyId(kid);
            if (jwk == null) {
                throw new Exception("토큰의 'kid'에 해당하는 공개 키를 찾을 수 없습니다: " + kid);
            }
            RSAPublicKey publicKey = jwk.toRSAKey().toRSAPublicKey();

            JWSVerifier verifier = new RSASSAVerifier(publicKey);
            if (!signedJWT.verify(verifier)) {
                throw new Exception("토큰 서명 검증에 실패했습니다.");
            }

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            String provider = "google";
            String providerId = claims.getStringClaim("sub");
            String extractedName = claims.getStringClaim("name");
            String loginId = provider + "_" + providerId;

            Optional<User> optionalUser = userRepository.findByLoginId(loginId);
            if (optionalUser.isEmpty()) {
                User newUser = User.builder()
                        .loginId(loginId)
                        .name(extractedName)
                        .provider(provider)
                        .providerId(providerId)
                        .role(UserRole.USER)
                        .build();
                userRepository.save(newUser);
            }
            return loginId;
        } catch (Exception e) {
            throw new RuntimeException("Google ID 토큰 처리 실패", e);
        }
    }
}

