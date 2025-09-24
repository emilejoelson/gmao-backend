package com.project.gmao.features.authentication.services.implementation;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.gmao.common.constants.Constants;
import com.project.gmao.common.enums.RoleEnum;
import com.project.gmao.core.exception.AuthenticationException;
import com.project.gmao.core.exception.ElementAlreadyExistsException;
import com.project.gmao.core.exception.ElementNotFoundException;
import com.project.gmao.core.exception.InvalidTokenException;
import com.project.gmao.core.security.JwtTokenProvider;
import com.project.gmao.core.security.UserPrincipal;
import com.project.gmao.features.authentication.dto.request.LoginRequest;
import com.project.gmao.features.authentication.dto.request.RefreshTokenRequest;
import com.project.gmao.features.authentication.dto.request.SignupRequest;
import com.project.gmao.features.authentication.dto.response.AuthResponse;
import com.project.gmao.features.authentication.dto.response.UserResponse;
import com.project.gmao.features.authentication.entity.User;
import com.project.gmao.features.authentication.mapper.UserMapper;
import com.project.gmao.features.authentication.repository.UserRepository;
import com.project.gmao.features.authentication.services.interfaces.AuthenticationService;
import com.project.gmao.features.manage_role.entity.Role;
import com.project.gmao.features.manage_role.repository.RoleRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

            @Override
        @Transactional
        public UserResponse signup(SignupRequest request) throws ElementAlreadyExistsException {
            validateSignupRequest(request);
            
            userRepository.findByEmailIgnoreCase(request.email()).ifPresent(existingUser -> {
                throw new ElementAlreadyExistsException(Constants.ALREADY_EXISTS);
            });
            
            User user = userMapper.signupRequestToUser(request);
            user.setPassword(passwordEncoder.encode(request.motDePasse()));
            
            // Initialize roles set since it's ignored in mapper
            user.setRoles(new HashSet<>());
            
            Role defaultRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new ElementNotFoundException("Default role not found"));
            user.getRoles().add(defaultRole);
            
            User savedUser = userRepository.save(user);
            
            return userMapper.userToUserResponse(savedUser);
        }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) throws AuthenticationException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.motDePasse())
            );

            String accessToken = tokenProvider.generateAccessToken(authentication);
            String refreshToken = tokenProvider.generateRefreshToken(authentication);
            
            User user = userRepository.findByEmailIgnoreCase(request.email())
                    .orElseThrow(() -> new AuthenticationException(Constants.USER_NOT_FOUND));

            refreshTokenService.createRefreshToken(user, refreshToken);
            
            return new AuthResponse(
                accessToken,
                refreshToken,
                15 * 60,
                userMapper.userToUserResponse(user)
            );

        } catch (BadCredentialsException e) {
            throw new AuthenticationException(Constants.INVALID_CREDENTIALS);
        }
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) throws InvalidTokenException, AuthenticationException {
        String refreshTokenValue = request.refreshToken();
        
        if (!tokenProvider.isTokenValid(refreshTokenValue) || tokenProvider.isTokenExpired(refreshTokenValue)) {
            throw new InvalidTokenException(Constants.INVALID_TOKEN);
        }

        if (!tokenProvider.getTokenType(refreshTokenValue).name().equals("REFRESH")) {
            throw new InvalidTokenException(Constants.INVALID_TOKEN);
        }

        String email = tokenProvider.getEmailFromToken(refreshTokenValue);
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AuthenticationException(Constants.USER_NOT_FOUND));

        refreshTokenService.validateRefreshToken(refreshTokenValue, user);

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userPrincipal, 
            null, 
            userPrincipal.getAuthorities() 
        );

        String newAccessToken = tokenProvider.generateAccessToken(authentication);
        String newRefreshToken = tokenProvider.generateRefreshToken(authentication);

        refreshTokenService.rotateRefreshToken(user, refreshTokenValue, newRefreshToken);

        return new AuthResponse(
            newAccessToken,
            newRefreshToken,
            15 * 60,
            userMapper.userToUserResponse(user)
        );
    }

    @Override
    @Transactional
    public void logout(String email) throws AuthenticationException {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AuthenticationException(Constants.USER_NOT_FOUND));
        
        refreshTokenService.deleteUserRefreshTokens(user);
    }

    @Override
    public User findByEmail(String email) throws ElementNotFoundException {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ElementNotFoundException(Constants.NOT_FOUND));
    }

    private void validateSignupRequest(SignupRequest request) {
        if (!request.motDePasse().equals(request.confirmerMotDePasse())) {
            throw new IllegalArgumentException(Constants.PASSWORD_MISMATCH);
        }
    }
}