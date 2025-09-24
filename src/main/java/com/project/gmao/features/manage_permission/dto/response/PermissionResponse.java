package com.project.gmao.features.manage_permission.dto.response;

import java.io.Serializable;
import java.util.UUID;

import com.project.gmao.common.enums.PermissionEnum;
import com.project.gmao.features.authentication.dto.response.BaseDto;

public record PermissionResponse(
    Long id,
    UUID uuid,
    String name,
    String description,
    boolean enabled,
    BaseDto baseDto
) implements Serializable {
}
