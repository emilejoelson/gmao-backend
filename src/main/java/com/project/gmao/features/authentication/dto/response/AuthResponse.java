package com.project.gmao.features.authentication.dto.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthResponse(
    @JsonProperty("access_token")
    String accessToken,
    
    @JsonProperty("refresh_token") 
    String refreshToken,
    
    @JsonProperty("token_type")
    String tokenType,
    
    @JsonProperty("expires_in")
    long expiresIn,
    
    UserResponse user
) implements Serializable {
    
    // Constructor that sets default tokenType
    public AuthResponse(String accessToken, String refreshToken, long expiresIn, UserResponse user) {
        this(accessToken, refreshToken, "Bearer", expiresIn, user);
    }
}