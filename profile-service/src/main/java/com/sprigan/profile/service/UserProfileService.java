package com.sprigan.profile.service;

import com.sprigan.profile.exception.AppException;
import com.sprigan.profile.exception.ErrorCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sprigan.profile.dto.request.ProfileCreationRequest;
import com.sprigan.profile.dto.response.UserProfileResponse;
import com.sprigan.profile.entity.UserProfile;
import com.sprigan.profile.mapper.UserProfileMapper;
import com.sprigan.profile.repository.UserProfileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    public UserProfileResponse createProfile(ProfileCreationRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile = userProfileRepository.save(userProfile);

        return userProfileMapper.toUserProfileReponse(userProfile);
    }

    public UserProfileResponse getByUserId(String userId) {
        UserProfile userProfile =
                userProfileRepository.findByUserId(userId)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userProfileMapper.toUserProfileReponse(userProfile);
    }

    public UserProfileResponse getProfile(String id) {
        UserProfile userProfile =
                userProfileRepository.findById(id).orElseThrow(
                        () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userProfileMapper.toUserProfileReponse(userProfile);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getAllProfiles() {
        var profiles = userProfileRepository.findAll();

        return profiles.stream().map(userProfileMapper::toUserProfileReponse).toList();
    }

    public UserProfileResponse getMyProfile() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        var profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userProfileMapper.toUserProfileReponse(profile);
    }
}
