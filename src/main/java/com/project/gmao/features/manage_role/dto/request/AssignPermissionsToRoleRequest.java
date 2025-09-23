package com.project.gmao.features.manage_role.dto.request;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;

public record AssignPermissionsToRoleRequest(
    @NotEmpty(message = "Au moins une permission doit être spécifiée")
    Set<UUID> permissionUuids
) implements Serializable {
}