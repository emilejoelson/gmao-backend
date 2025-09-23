package com.project.gmao.core.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.project.gmao.shared.dto.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .success(false)
                .message("Erreurs de validation")
                .data(errors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ElementAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserAlreadyExists(ElementAlreadyExistsException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage(), "USER_ALREADY_EXISTS");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(Exception ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage(), "AUTHENTICATION_FAILED");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidToken(InvalidTokenException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage(), "INVALID_TOKEN");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage(), "BAD_REQUEST");
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        log.error("Unhandled exception occurred", ex);
        ApiResponse<Void> response = ApiResponse.error(
                "Une erreur interne s'est produite", 
                "INTERNAL_SERVER_ERROR"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
