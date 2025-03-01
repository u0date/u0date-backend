package com.u0date.u0date_backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RefreshTokenDto {
    @NotEmpty(message = "refresh token cannot be null or empty")
    private String refreshToken;
}
gi