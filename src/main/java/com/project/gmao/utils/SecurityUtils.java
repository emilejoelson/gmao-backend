package com.project.gmao.utils;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.project.gmao.core.security.UserPrincipal;

import java.util.Optional;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static Optional<UserPrincipal> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return Optional.of((UserPrincipal) authentication.getPrincipal());
        }
        return Optional.empty();
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().map(UserPrincipal::getId).orElse(null);
    }

    public static String getCurrentUserEmail() {
        return getCurrentUser().map(UserPrincipal::getEmail).orElse(null);
    }

    public static boolean isCurrentUser(Long userId) {
        Long currentUserId = getCurrentUserId();
        return currentUserId != null && currentUserId.equals(userId);
    }
}
