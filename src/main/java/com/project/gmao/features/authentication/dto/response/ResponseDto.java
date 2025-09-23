package com.project.gmao.features.authentication.dto.response;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

public record ResponseDto(
    HttpStatus status,
    String message
) implements Serializable {
}

