package com.u0date.u0date_backend.service.impl;

import com.u0date.u0date_backend.dto.DefaultApiResponse;
import com.u0date.u0date_backend.dto.LoginRequestDto;
import com.u0date.u0date_backend.dto.LoginResponseDto;
import com.u0date.u0date_backend.service.IAuthenticationService;
import com.u0date.u0date_backend.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final JWTService jwtService;

    @Override
    public DefaultApiResponse<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
        DefaultApiResponse<LoginResponseDto> response = new DefaultApiResponse<>();
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setAccessToken(jwtService.generateToken(loginRequestDto.getIdentity()));
        response.setStatusCode(HttpStatus.OK.value());
        response.setStatusMessage("Login Successful");
        response.setData(loginResponseDto);
        return response;
    }
}
