package com.harinem.post_service.repository.httpclient;

import com.harinem.post_service.dto.response.ApiResponse;
import com.harinem.post_service.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service",url = "${app.services.profile.url}")
public interface ProfileClient {
    @GetMapping(value = "/internal/users/{userId}",produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserProfileResponse> getProfileByUserId(@PathVariable String userId);

}

