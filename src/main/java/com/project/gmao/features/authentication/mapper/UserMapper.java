package com.project.gmao.features.authentication.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.project.gmao.features.authentication.dto.request.SignupRequest;
import com.project.gmao.features.authentication.dto.response.BaseDto;
import com.project.gmao.features.authentication.dto.response.UserResponse;
import com.project.gmao.features.authentication.entity.User;
import com.project.gmao.features.manage_permission.dto.response.PermissionResponse;
import com.project.gmao.features.manage_permission.entity.Permission;
import com.project.gmao.features.manage_permission.mapper.PermissionMapper;
import com.project.gmao.features.manage_role.dto.response.RoleResponse;
import com.project.gmao.features.manage_role.entity.Role;
import com.project.gmao.features.manage_role.mapper.RoleMapper;

@Mapper(uses = {RoleMapper.class, PermissionMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "uuid", ignore = true),
        @Mapping(target = "password", ignore = true),
        @Mapping(target = "roles", ignore = true),
        @Mapping(target = "enabled", ignore = true),
        @Mapping(target = "prenom", expression = "java(request.prenom() != null ? request.prenom().trim() : null)"),
        @Mapping(target = "nom", expression = "java(request.nom() != null ? request.nom().trim() : null)"),
        @Mapping(target = "email", expression = "java(request.email() != null ? request.email().toLowerCase().trim() : null)"),
        @Mapping(target = "telephone", expression = "java(request.telephone() != null ? request.telephone().trim() : null)")
    })
    User signupRequestToUser(SignupRequest request);

    @Mappings({
        @Mapping(target = "baseDto", expression = "java(toBaseDto(user))"),
        @Mapping(target = "roles", source = "roles"),
        @Mapping(target = "permissions", expression = "java(mapPermissions(user.getAllPermissions()))")
    })
    UserResponse userToUserResponse(User user);

    // Add these mapping methods for nested objects
    default Set<RoleResponse> mapRoles(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(RoleMapper.INSTANCE::roleToRoleResponse)
                .collect(Collectors.toSet());
    }

    default Set<PermissionResponse> mapPermissions(Set<Permission> permissions) {
        if (permissions == null) {
            return null;
        }
        return permissions.stream()
                .map(PermissionMapper.INSTANCE::permissionToPermissionResponse)
                .collect(Collectors.toSet());
    }

    default BaseDto toBaseDto(User user) {
        return new BaseDto(user.getCreatedAt(), user.getUpdatedAt());
    }
}