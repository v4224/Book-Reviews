package com.sprigan.identity_service.repository.httpclient;

import com.sprigan.identity_service.configuration.AuthenticationRequestInterceptor;
import com.sprigan.identity_service.dto.request.ApiResponse;
import com.sprigan.identity_service.dto.request.ProfileCreationRequest;
import com.sprigan.identity_service.dto.response.UserProfileResponse;
import org.springframework.http.MediaType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", url = "${app.services.profile}",
        configuration = { AuthenticationRequestInterceptor.class })
public interface ProfileClient {
    @PostMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserProfileResponse> createProfile(@RequestBody ProfileCreationRequest request);
}
