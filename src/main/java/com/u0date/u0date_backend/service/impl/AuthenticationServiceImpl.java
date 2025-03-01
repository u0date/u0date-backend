package com.u0date.u0date_backend.service.impl;

import com.u0date.u0date_backend.dto.DefaultApiResponse;
import com.u0date.u0date_backend.dto.LoginRequestDto;
import com.u0date.u0date_backend.dto.LoginResponseDto;
import com.u0date.u0date_backend.entity.Account;
import com.u0date.u0date_backend.exception.ResourceNotFound;
import com.u0date.u0date_backend.repository.AccountRepository;
import com.u0date.u0date_backend.service.IAuthenticationService;
import com.u0date.u0date_backend.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final JWTService jwtService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public DefaultApiResponse<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
        DefaultApiResponse<LoginResponseDto> response = new DefaultApiResponse<>();
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setAccessToken(jwtService.generateToken(verifyAccount(loginRequestDto).getEmail()));
        response.setStatusCode(HttpStatus.OK.value());
        response.setStatusMessage("Login Successful");
        response.setData(loginResponseDto);
        return response;
    }

    private Account verifyAccount(LoginRequestDto loginRequestDto){
        Account account = accountRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(()
                -> new ResourceNotFound("Email Not Found!"));
        if(!passwordEncoder.matches(loginRequestDto.getPassword(), account.getPassword()))
            throw new BadCredentialsException("Invalid password");
        return account;
    }
}
