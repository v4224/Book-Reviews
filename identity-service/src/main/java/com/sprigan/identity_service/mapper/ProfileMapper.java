package com.sprigan.identity_service.mapper;

import com.sprigan.identity_service.dto.request.ProfileCreationRequest;
import com.sprigan.identity_service.dto.request.UserCreationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest(UserCreationRequest request);
}