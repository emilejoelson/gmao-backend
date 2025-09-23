package com.project.gmao.features.authentication.dto.request;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;

public record AssignRolesToUserRequest(
    @NotEmpty(message = "Au moins un rôle doit être spécifié")
    Set<UUID> roleUuids
) implements Serializable {
}