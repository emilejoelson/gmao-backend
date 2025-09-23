package com.project.gmao.features.authentication.dto.request;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import com.project.gmao.common.enums.RoleEnum;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserRoleAssignmentRequest(
    @NotNull(message = "L'UUID de l'utilisateur est obligatoire")
    UUID userUuid,

    @NotEmpty(message = "Au moins un rôle doit être spécifié")
    Set<RoleEnum> roles
) implements Serializable {
}
