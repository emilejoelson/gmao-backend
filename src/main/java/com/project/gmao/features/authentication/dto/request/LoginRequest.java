package com.project.gmao.features.authentication.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;



/**
 * A DTO for the {@link User} entity
 */

public record LoginRequest(
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    String email,

    @NotBlank(message = "Le mot de passe est obligatoire")
    String motDePasse
) implements Serializable {
}
