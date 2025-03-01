package com.u0date.u0date_backend.controller.auth;

import com.u0date.u0date_backend.dto.DefaultApiResponse;
import com.u0date.u0date_backend.dto.AuthResponseDto;
import com.u0date.u0date_backend.dto.LoginRequestDto;
import com.u0date.u0date_backend.dto.RefreshTokenDto;
import com.u0date.u0date_backend.service.IAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {
    private final IAuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<DefaultApiResponse<AuthResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.status(HttpStatus.OK).body(
                authenticationService.login(loginRequestDto)
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<DefaultApiResponse<AuthResponseDto>> refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto){
        return ResponseEntity.status(HttpStatus.OK).body(
                authenticationService.refreshToken(refreshTokenDto)
        );
    }
}
