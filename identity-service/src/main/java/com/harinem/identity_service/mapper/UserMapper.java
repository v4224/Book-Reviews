package com.harinem.identity_service.mapper;

import com.harinem.identity_service.dto.request.UserCreationRequest;
import com.harinem.identity_service.dto.request.UserUpdateRequest;
import com.harinem.identity_service.dto.response.ProfileCreationResponse;
import com.harinem.identity_service.dto.response.UserCreationResponse;
import com.harinem.identity_service.dto.response.UserResponse;
import com.harinem.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mappings({
            @Mapping(source = "user.id", target = "id"),
            @Mapping(source = "user.username", target = "username"),
            @Mapping(source = "user.roles",target = "roles"),
            @Mapping(source = "user.email",target = "email"),
            @Mapping(source = "user.emailVerified",target = "emailVerified"),
            @Mapping(source = "profile.firstName", target = "firstName"),
            @Mapping(source = "profile.lastName", target = "lastName"),
            @Mapping(source = "profile.dob", target = "dob")
    })
    UserCreationResponse toUserCreationResponse(User user, ProfileCreationResponse profile);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
