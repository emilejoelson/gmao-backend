package com.project.gmao.features.manage_permission.dto.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PermissionCreateRequest(
    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    String name,

    @Size(max = 255, message = "La description ne peut pas dépasser 255 caractères")
    String description
) implements Serializable {
}