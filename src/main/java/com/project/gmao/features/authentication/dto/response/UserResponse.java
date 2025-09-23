package com.project.gmao.features.authentication.dto.response;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import com.project.gmao.common.enums.RoleEnum;
import com.project.gmao.features.manage_permission.dto.response.PermissionResponse;
import com.project.gmao.features.manage_role.dto.response.RoleResponse;

/**
 * A DTO for the {@link User} entity
 */

public record UserResponse(
    Long id,
    UUID uuid,
    String prenom,
    String nom,
    String email,
    String telephone,
    String fullName,
    Set<RoleResponse> roles,
    Set<PermissionResponse> permissions,
    BaseDto baseDto
) implements Serializable {
}
