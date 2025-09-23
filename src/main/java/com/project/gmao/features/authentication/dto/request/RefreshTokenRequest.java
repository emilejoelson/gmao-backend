package com.project.gmao.features.authentication.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

public record RefreshTokenRequest(
    @NotBlank(message = "Le refresh token est obligatoire")
    String refreshToken
) implements Serializable {
}
