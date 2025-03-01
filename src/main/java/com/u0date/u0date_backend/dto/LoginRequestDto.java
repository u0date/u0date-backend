package com.u0date.u0date_backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotEmpty(message = "email cannot be null or empty")
    @Email(message = "email address format is invalid")
    private String email;

    @Size(min = 8, message = "password must be at least 8 characters long")
    @NotEmpty(message = "password cannot be null or empty")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String password;
}
