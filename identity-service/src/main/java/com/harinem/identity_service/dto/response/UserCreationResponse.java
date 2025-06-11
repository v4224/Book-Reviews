package com.harinem.identity_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    String email;
    boolean emailVerified;
    LocalDate dob;
    Set<RoleResponse> roles;
}
