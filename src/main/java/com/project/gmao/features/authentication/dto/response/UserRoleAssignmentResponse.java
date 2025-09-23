package com.project.gmao.features.authentication.dto.response;

import java.io.Serializable;

public record UserRoleAssignmentResponse(
    UserResponse user,
    AuthResponse newTokens
) implements Serializable {
}