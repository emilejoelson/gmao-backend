package com.project.gmao.core.security;

import com.project.gmao.common.enums.TokenType;
import com.project.gmao.core.config.JwtConfig;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import static com.project.gmao.common.constants.SecurityConstants.CLAIM_EMAIL;
import static com.project.gmao.common.constants.SecurityConstants.CLAIM_ROLES;
import static com.project.gmao.common.constants.SecurityConstants.CLAIM_TOKEN_TYPE;
import static com.project.gmao.common.constants.SecurityConstants.CLAIM_USER_ID;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, TokenType.ACCESS, jwtConfig.getAccessTokenExpiration());
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, TokenType.REFRESH, jwtConfig.getRefreshTokenExpiration());
    }

    private String generateToken(Authentication authentication, TokenType tokenType, long expiration) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date expiryDate = new Date(System.currentTimeMillis() + expiration);

        JwtBuilder builder = Jwts.builder()
                .subject(userPrincipal.getEmail())
                .claim("userId", userPrincipal.getId())
                .claim("email", userPrincipal.getEmail())
                .claim("tokenType", tokenType.name())
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS256);

        if (tokenType == TokenType.ACCESS) {
            Set<String> authorities = userPrincipal.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .collect(Collectors.toSet());
            builder.claim("authorities", authorities);
        }

        return builder.compact();
    }

    public String getEmailFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    public TokenType getTokenType(String token) {
        Claims claims = getClaimsFromToken(token);
        String tokenType = claims.get("tokenType", String.class);
        return TokenType.valueOf(tokenType);
    }

    public boolean isTokenValid(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getExpiration().before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}