package com.project.gmao.features.manage_role.dto.response;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import com.project.gmao.common.enums.RoleEnum;
import com.project.gmao.features.authentication.dto.response.BaseDto;
import com.project.gmao.features.manage_permission.dto.response.PermissionResponse;

public record RoleResponse(
    Long id,
    UUID uuid,
    RoleEnum name,
    String description,
    boolean enabled,
    Set<PermissionResponse> permissions,
    BaseDto baseDto
) implements Serializable {
}
