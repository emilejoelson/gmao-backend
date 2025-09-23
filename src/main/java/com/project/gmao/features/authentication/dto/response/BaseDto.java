package com.project.gmao.features.authentication.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;

public record BaseDto(
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {}

