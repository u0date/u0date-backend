package com.u0date.u0date_backend.service;

import com.u0date.u0date_backend.dto.DefaultApiResponse;
import com.u0date.u0date_backend.dto.AuthResponseDto;
import com.u0date.u0date_backend.dto.LoginRequestDto;
import com.u0date.u0date_backend.dto.RefreshTokenDto;
import jakarta.validation.Valid;

public interface IAuthenticationService {
    DefaultApiResponse<AuthResponseDto> login(@Valid LoginRequestDto authRequestDto, String deviceId);
    DefaultApiResponse<AuthResponseDto> refreshToken(@Valid RefreshTokenDto refreshTokenDto, String deviceId);
}
