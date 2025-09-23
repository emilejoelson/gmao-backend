package com.project.gmao.features.authentication.dto.request;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import com.project.gmao.shared.validation.ValidEmail;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    @Size(max = 100, message = "Le prénom ne peut pas dépasser 100 caractères")
    String prenom,

    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    String nom,

    @ValidEmail
    String email,

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Numéro de téléphone invalide")
    String telephone,

    Boolean enabled,

    Set<UUID> roleUuids
) implements Serializable {
}