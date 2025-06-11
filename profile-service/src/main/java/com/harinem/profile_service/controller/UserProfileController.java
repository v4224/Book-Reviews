package com.harinem.profile_service.controller;

import com.harinem.profile_service.dto.request.UpdateProfileRequest;
import com.harinem.profile_service.dto.request.UserProfileCreationRequest;
import com.harinem.profile_service.dto.response.ApiResponse;
import com.harinem.profile_service.dto.response.UserProfileCreationResponse;
import com.harinem.profile_service.dto.response.UserProfileResponse;
import com.harinem.profile_service.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileController {

    UserProfileService userProfileService;

    @GetMapping("/users/{profileId}")
    UserProfileCreationResponse getProfileById(@PathVariable String profileId){
        return userProfileService.getProfile(profileId);
    }

    @GetMapping("/users")
    List<UserProfileCreationResponse> getAll(){
        return userProfileService.getAll();
    }

    @GetMapping("/users/my-profile")
    ApiResponse<UserProfileResponse> getMyProfile(){
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.getMyProfile())
                .build();
    }

    @PutMapping("/users/my-profile")
    ApiResponse<UserProfileResponse> updateMyProfile(@RequestBody UpdateProfileRequest request){
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.updateMyProfile(request))
                .build();

    }

    @PutMapping("/users/avatar")
    ApiResponse<UserProfileResponse> updateAvatar(@RequestParam("file")MultipartFile file){
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.updateAvatar(file))
                .build();

    }




}
