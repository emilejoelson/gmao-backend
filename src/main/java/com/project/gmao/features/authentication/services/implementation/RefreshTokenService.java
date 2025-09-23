package com.project.gmao.features.authentication.services.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.gmao.core.config.JwtConfig;
import com.project.gmao.core.exception.InvalidTokenException;
import com.project.gmao.features.authentication.entity.RefreshToken;
import com.project.gmao.features.authentication.entity.User;
import com.project.gmao.features.authentication.repository.RefreshTokenRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtConfig jwtConfig;

    @Transactional
    public void createRefreshToken(User user, String tokenValue) {
        // Remove existing refresh tokens for this user
        refreshTokenRepository.deleteByUser(user);

        LocalDateTime expiryDate = LocalDateTime.now()
                .plusNanos(jwtConfig.getRefreshTokenExpiration() * 1_000_000);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(tokenValue)
                .user(user)
                .expiryDate(expiryDate)
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void validateRefreshToken(String tokenValue, User user) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new InvalidTokenException("Refresh token non trouvé"));

        if (!refreshToken.getUser().getId().equals(user.getId())) {
            throw new InvalidTokenException("Refresh token ne correspond pas à l'utilisateur");
        }

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidTokenException("Refresh token expiré");
        }
    }

    @Transactional
    public void rotateRefreshToken(User user, String oldToken, String newToken) {
        // Delete old refresh token
        refreshTokenRepository.findByToken(oldToken)
                .ifPresent(refreshTokenRepository::delete);

        // Create new refresh token
        createRefreshToken(user, newToken);
    }

    @Transactional
    public void deleteUserRefreshTokens(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    @Scheduled(fixedRate = 60 * 60 * 1000) // Run every hour
    @Transactional
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        refreshTokenRepository.deleteExpiredTokens(now);
        
    }
}
