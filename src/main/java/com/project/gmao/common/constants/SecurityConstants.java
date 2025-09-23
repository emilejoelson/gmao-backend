package com.project.gmao.common.constants;

public final class SecurityConstants {
    
    // JWT Configuration
    public static final String JWT_SECRET_KEY = "${app.jwt.secret:mySecretKey}";
    public static final long JWT_ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000L; // 15 minutes
    public static final long JWT_REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000L; // 7 days
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String JWT_HEADER_STRING = "Authorization";
    public static final String JWT_REFRESH_HEADER = "X-Refresh-Token";
    
    // Security Endpoints
    public static final String[] PUBLIC_ENDPOINTS = {
        "/auth/**",
    };
    
    // Claims
    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_ROLES = "roles";
    public static final String CLAIM_TOKEN_TYPE = "tokenType";
    
    // Token Types
    public static final String ACCESS_TOKEN_TYPE = "ACCESS";
    public static final String REFRESH_TOKEN_TYPE = "REFRESH";
    
    // Password Constraints
    public static final int MIN_PASSWORD_LENGTH = 6;
    
    private SecurityConstants() {}
}