package com.u0date.u0date_backend.service;

import com.u0date.u0date_backend.dto.DefaultApiResponse;
import com.u0date.u0date_backend.dto.LoginRequestDto;
import com.u0date.u0date_backend.dto.LoginResponseDto;

public interface IAuthenticationService {
    public DefaultApiResponse<LoginResponseDto> login(LoginRequestDto loginRequestDto);
}
