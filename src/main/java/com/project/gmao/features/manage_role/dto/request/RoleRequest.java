package com.project.gmao.features.manage_role.dto.request;

import java.io.Serializable;
import java.util.Set;

import com.project.gmao.common.enums.PermissionEnum;
import com.project.gmao.common.enums.RoleEnum;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RoleRequest(
    @NotNull(message = "Le nom du rôle est obligatoire")
    RoleEnum name,

    @Size(max = 255, message = "La description ne peut pas dépasser 255 caractères")
    String description,

    Set<PermissionEnum> permissions
) implements Serializable {
}
