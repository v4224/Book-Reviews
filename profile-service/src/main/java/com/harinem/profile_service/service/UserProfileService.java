package com.harinem.profile_service.service;

import com.harinem.profile_service.dto.request.UpdateProfileRequest;
import com.harinem.profile_service.dto.request.UserProfileCreationRequest;
import com.harinem.profile_service.dto.response.UserProfileCreationResponse;
import com.harinem.profile_service.dto.response.UserProfileResponse;
import com.harinem.profile_service.entity.UserProfile;
import com.harinem.profile_service.exception.AppException;
import com.harinem.profile_service.exception.ErrorCode;
import com.harinem.profile_service.mapper.UserProfileMapper;
import com.harinem.profile_service.repository.UserProfileRepository;
import com.harinem.profile_service.repository.httpclient.FileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileService {

    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;
    FileClient fileClient;

    public UserProfileCreationResponse createProfile(UserProfileCreationRequest request){
        UserProfile userProfile=userProfileMapper.toUserProfile(request);
        userProfile=userProfileRepository.save(userProfile);
        return userProfileMapper.toUserProfileCreationResponse(userProfile);
    }

    public UserProfileCreationResponse getProfile(String id){
        UserProfile userProfile=userProfileRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        return userProfileMapper.toUserProfileCreationResponse(userProfile);

    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileCreationResponse> getAll(){
        return userProfileRepository.findAll().stream().map(userProfileMapper::toUserProfileCreationResponse).toList();
    }

    public UserProfileResponse getMyProfile(){
        var authentication= SecurityContextHolder.getContext().getAuthentication();
        String userId= authentication.getName();
        var profile=userProfileRepository.findByUserId(userId).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
        return userProfileMapper.toUserProfileResponse(profile);
    }

    public UserProfileResponse getProfileByUserId(String userId){
        UserProfile userProfile =
                userProfileRepository.findByUserId(userId).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));

        return userProfileMapper.toUserProfileResponse(userProfile);

    }

    public UserProfileResponse updateMyProfile(UpdateProfileRequest request){
        var authentication=SecurityContextHolder.getContext().getAuthentication();
        String userId=authentication.getName();
        var profile=userProfileRepository.findByUserId(userId)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        userProfileMapper.update(profile,request);
        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(profile));

    }


    public UserProfileResponse updateAvatar(MultipartFile file) {
        var authentication=SecurityContextHolder.getContext().getAuthentication();
        String userId=authentication.getName();

        var profile=userProfileRepository.findByUserId(userId)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        //Upload file - invoke an api on file-service

        var response=fileClient.uploadMedia(file);

        profile.setAvatar(response.getResult().getUrl());

        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(profile));
    }
}
