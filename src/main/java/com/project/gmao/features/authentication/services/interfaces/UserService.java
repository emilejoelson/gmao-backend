package com.project.gmao.features.authentication.services.interfaces;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.project.gmao.common.enums.RoleEnum;
import com.project.gmao.features.authentication.dto.request.SignupRequest;
import com.project.gmao.features.authentication.dto.request.UserRoleAssignmentRequest;
import com.project.gmao.features.authentication.dto.response.UserResponse;
public interface UserService {
    UserResponse getUserById(Long id);
    UserResponse getUserByUuid(UUID uuid);
    List<UserResponse> getAllUsers();
    UserResponse updateUser(UUID uuid, SignupRequest request);
    void deleteUser(UUID uuid);
    UserResponse assignRolesToUser(UserRoleAssignmentRequest request);
    UserResponse removeRolesFromUser(UUID userUuid, Set<String> roles);
}