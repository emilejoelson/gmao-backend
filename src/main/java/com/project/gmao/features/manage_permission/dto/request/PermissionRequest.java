package com.project.gmao.features.manage_permission.dto.request;

import java.io.Serializable;

import com.project.gmao.common.enums.PermissionEnum;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PermissionRequest(
    @NotNull(message = "Le nom de la permission est obligatoire")
    String name,

    @Size(max = 255, message = "La description ne peut pas dépasser 255 caractères")
    String description
) implements Serializable {
}