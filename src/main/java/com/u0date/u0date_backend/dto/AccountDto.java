package com.u0date.u0date_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {
    @Size(min = 3, message = "username must be at least 3 characters long")
    @NotEmpty(message = "username cannot be null or empty")
    private String username;

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

