package com.harinem.profile_service.controller;


import com.harinem.profile_service.dto.request.UserProfileCreationRequest;
import com.harinem.profile_service.dto.response.ApiResponse;
import com.harinem.profile_service.dto.response.UserProfileCreationResponse;
import com.harinem.profile_service.dto.response.UserProfileResponse;
import com.harinem.profile_service.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InternalUserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/internal/users")
    ApiResponse<UserProfileCreationResponse> createProfile(@RequestBody UserProfileCreationRequest request){
        return ApiResponse.<UserProfileCreationResponse>builder()
                .result(userProfileService.createProfile(request))
                .build();

    }
    @GetMapping("/internal/users/{userId}")
    ApiResponse<UserProfileResponse> getProfileByUserId(@PathVariable String userId){
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.getProfileByUserId(userId))
                .build();
    }

}
