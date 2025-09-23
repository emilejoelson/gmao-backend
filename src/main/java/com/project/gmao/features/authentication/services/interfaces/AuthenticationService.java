package com.project.gmao.features.authentication.services.interfaces;

import com.project.gmao.core.exception.AuthenticationException;
import com.project.gmao.core.exception.ElementAlreadyExistsException;
import com.project.gmao.core.exception.ElementNotFoundException;
import com.project.gmao.core.exception.InvalidTokenException;
import com.project.gmao.features.authentication.dto.request.LoginRequest;
import com.project.gmao.features.authentication.dto.request.RefreshTokenRequest;
import com.project.gmao.features.authentication.dto.request.SignupRequest;
import com.project.gmao.features.authentication.dto.response.AuthResponse;
import com.project.gmao.features.authentication.dto.response.UserResponse;
import com.project.gmao.features.authentication.entity.User;

public interface AuthenticationService {
    UserResponse signup(SignupRequest request) throws ElementAlreadyExistsException;
    AuthResponse login(LoginRequest request) throws AuthenticationException;
    AuthResponse refreshToken(RefreshTokenRequest request) throws InvalidTokenException, AuthenticationException;
    void logout(String email) throws AuthenticationException;
    User findByEmail(String email) throws ElementNotFoundException;
}
