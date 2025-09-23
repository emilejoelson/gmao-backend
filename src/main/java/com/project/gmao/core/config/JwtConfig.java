package com.project.gmao.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtConfig {
    private String secret = "mySecretKey";
    private long accessTokenExpiration = 15 * 60 * 1000L; // 15 minutes
    private long refreshTokenExpiration = 7 * 24 * 60 * 60 * 1000L; // 7 days
}