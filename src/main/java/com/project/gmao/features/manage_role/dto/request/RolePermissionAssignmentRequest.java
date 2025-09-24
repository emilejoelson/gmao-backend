package com.project.gmao.features.manage_role.dto.request;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import com.project.gmao.common.enums.PermissionEnum;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RolePermissionAssignmentRequest(
    @NotNull(message = "L'UUID du rôle est obligatoire")
    UUID roleUuid,

    @NotEmpty(message = "Au moins une permission doit être spécifiée")
    Set<String> permissions
) implements Serializable {
}