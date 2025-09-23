package com.project.gmao.features.authentication.dto.request;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.io.Serializable;

import com.project.gmao.shared.validation.ValidEmail;
import com.project.gmao.shared.validation.ValidPassword;

/**
 * A DTO for the {@link User} entity
 */

public record SignupRequest(
    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 100, message = "Le prénom ne peut pas dépasser 100 caractères")
    String prenom,

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    String nom,

    @NotBlank(message = "L'email est obligatoire")
    @ValidEmail
    String email,

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Numéro de téléphone invalide")
    String telephone,

    @NotBlank(message = "Le mot de passe est obligatoire")
    @ValidPassword
    String motDePasse,

    @NotBlank(message = "La confirmation du mot de passe est obligatoire")
    String confirmerMotDePasse
) implements Serializable {
}