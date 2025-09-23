package com.project.gmao.features.authentication.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.gmao.common.constants.Constants;
import com.project.gmao.features.authentication.dto.request.LoginRequest;
import com.project.gmao.features.authentication.dto.request.RefreshTokenRequest;
import com.project.gmao.features.authentication.dto.request.SignupRequest;
import com.project.gmao.features.authentication.dto.response.AuthResponse;
import com.project.gmao.features.authentication.dto.response.ResponseDto;
import com.project.gmao.features.authentication.dto.response.UserResponse;
import com.project.gmao.features.authentication.entity.User;
import com.project.gmao.features.authentication.mapper.UserMapper;
import com.project.gmao.features.authentication.services.interfaces.AuthenticationService;
import com.project.gmao.shared.services.interfaces.EntityNameService;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final MessageSource messageSource;
    private final EntityNameService entityNameService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest request) {
        UserResponse user = authenticationService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authenticationService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse authResponse = authenticationService.refreshToken(request);
        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(Authentication authentication) {
        String email = authentication.getName();
        authenticationService.logout(email);
        
        String entityName = entityNameService.getEntityName(User.class);
        String logoutMessage = messageSource.getMessage(Constants.LOGOUT_SUCCESS, new Object[]{entityName}, LocaleContextHolder.getLocale());
        ResponseDto responseDto = new ResponseDto(HttpStatus.OK, logoutMessage);
        
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        User user = authenticationService.findByEmail(email);
        UserResponse userResponse = UserMapper.INSTANCE.userToUserResponse(user);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }
}