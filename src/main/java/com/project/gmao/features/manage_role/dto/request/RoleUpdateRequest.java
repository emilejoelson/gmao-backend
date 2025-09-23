package com.project.gmao.features.manage_role.dto.request;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoleUpdateRequest(
    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    String name,

    @Size(max = 255, message = "La description ne peut pas dépasser 255 caractères")
    String description,

    Boolean enabled,

    Set<UUID> permissionUuids
) implements Serializable {
}