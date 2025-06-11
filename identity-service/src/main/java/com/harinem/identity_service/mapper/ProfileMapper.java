package com.harinem.identity_service.mapper;


import com.harinem.identity_service.dto.request.ProfileCreationRequest;
import com.harinem.identity_service.dto.request.UserCreationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest(UserCreationRequest userCreationRequest);
}
