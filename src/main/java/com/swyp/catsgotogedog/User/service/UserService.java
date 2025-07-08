package com.swyp.catsgotogedog.User.service;



import com.swyp.catsgotogedog.User.domain.entity.User;
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
}

