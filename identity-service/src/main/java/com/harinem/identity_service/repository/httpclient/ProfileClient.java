package com.harinem.identity_service.repository.httpclient;

import com.harinem.identity_service.configuration.AuthenticationRequestInterceptor;
import com.harinem.identity_service.dto.request.ProfileCreationRequest;
import com.harinem.identity_service.dto.response.ApiResponse;
import com.harinem.identity_service.dto.response.ProfileCreationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name="profile-service",url="${app.services.profile}",configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {

    @PostMapping(value = "/internal/users",produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<ProfileCreationResponse> createProfile(@RequestBody ProfileCreationRequest request);


}
