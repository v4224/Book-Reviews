package com.harinem.identity_service.service;


import com.harinem.event.dto.NotificationEvent;
import com.harinem.identity_service.constant.PredefinedRole;
import com.harinem.identity_service.dto.request.UserCreationRequest;
import com.harinem.identity_service.dto.request.UserUpdateRequest;
import com.harinem.identity_service.dto.response.UserCreationResponse;
import com.harinem.identity_service.dto.response.UserResponse;
import com.harinem.identity_service.entity.Role;
import com.harinem.identity_service.entity.User;
import com.harinem.identity_service.exception.AppException;
import com.harinem.identity_service.exception.ErrorCode;
import com.harinem.identity_service.mapper.ProfileMapper;
import com.harinem.identity_service.mapper.UserMapper;
import com.harinem.identity_service.repository.RoleRepository;
import com.harinem.identity_service.repository.UserRepository;
import com.harinem.identity_service.repository.httpclient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;
    ProfileMapper profileMapper;
    KafkaTemplate<String,Object> kafkaTemplate;

    public UserCreationResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);

        user=userRepository.save(user);

        //profileClient.createProfile(profileMapper.toProfileCreationRequest(request));
        var profileRequest = profileMapper.toProfileCreationRequest(request);
        profileRequest.setUserId(user.getId());


        var profileResponse=profileClient.createProfile(profileRequest);
        //log.info(String.valueOf(profileResponse));

        //Build notification events
        NotificationEvent notificationEvent= NotificationEvent.builder()
                .channel("EMAIL")
                .recipient(request.getEmail())
                .subject("Welcome to book-harinem!!")
                .body("Hello "+request.getUsername()+" !")
                .build();

        //Publish message to Kafka
        kafkaTemplate.send("notification-delivery",notificationEvent);

        return userMapper.toUserCreationResponse(user,profileResponse.getResult());
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        //log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }
    @PreAuthorize("hasRole('ADMIN') or @userService.isSelf(#userId, authentication.name)")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }





}
