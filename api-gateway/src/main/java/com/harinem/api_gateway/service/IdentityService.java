package com.harinem.api_gateway.service;

import com.harinem.api_gateway.dto.request.IntrospectRequest;
import com.harinem.api_gateway.dto.response.ApiResponse;
import com.harinem.api_gateway.dto.response.IntrospectResponse;
import com.harinem.api_gateway.repository.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class IdentityService {

    IdentityClient identityClient;

    public Mono<ApiResponse<IntrospectResponse>> introspect(String token){
        return identityClient.introspect(IntrospectRequest.builder().token(token).build());
    }

}
