package com.u0date.u0date_backend.service.impl;

import com.u0date.u0date_backend.dto.DefaultApiResponse;
import com.u0date.u0date_backend.dto.AuthResponseDto;
import com.u0date.u0date_backend.dto.LoginRequestDto;
import com.u0date.u0date_backend.dto.RefreshTokenDto;
import com.u0date.u0date_backend.entity.Account;
import com.u0date.u0date_backend.exception.ResourceNotFound;
import com.u0date.u0date_backend.repository.AccountRepository;
import com.u0date.u0date_backend.service.AccountDetailsService;
import com.u0date.u0date_backend.service.IAuthenticationService;
import com.u0date.u0date_backend.service.JWTService;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final JWTService jwtService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountDetailsService accountDetailsService;

    @Override
    public DefaultApiResponse<AuthResponseDto> login(@Valid LoginRequestDto loginRequestDto) {
        DefaultApiResponse<AuthResponseDto> response = new DefaultApiResponse<>();
        AuthResponseDto authResponseDto = new AuthResponseDto();

        String tokenId = UUID.randomUUID().toString();
        Account account = verifyAccount(loginRequestDto);

        authResponseDto.setAccessToken(jwtService.generateToken(account.getEmail(), tokenId));
        authResponseDto.setRefreshToken(jwtService.generateRefreshToken(account.getEmail(), tokenId));
        account.setLastRefreshTokenId(tokenId);
        accountRepository.save(account);

        response.setStatusCode(HttpStatus.OK.value());
        response.setStatusMessage("Login Successful");
        response.setData(authResponseDto);
        return response;
    }

    @Override
    public DefaultApiResponse<AuthResponseDto> refreshToken(RefreshTokenDto refreshTokenDto) {
        DefaultApiResponse<AuthResponseDto> response = new DefaultApiResponse<>();
        AuthResponseDto authResponseDto = new AuthResponseDto();

        String email = jwtService.extractEmail(refreshTokenDto.getRefreshToken());
        if (email == null)
            throw new JwtException("An error occurred refreshing access token");
        if (!jwtService.isRefreshTokenValid(refreshTokenDto.getRefreshToken(), accountDetailsService.loadUserByUsername(email)))
            throw new JwtException("Invalid refresh token");

        Account account = accountRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFound("Invalid Email"));
        String tokenId = jwtService.extractTokenId(refreshTokenDto.getRefreshToken());

        if (!tokenId.equals(account.getLastRefreshTokenId()))
            throw new ResourceNotFound("Account not found for refresh token");

        String newTokenId = UUID.randomUUID().toString();
        authResponseDto.setAccessToken(jwtService.generateToken(email, newTokenId));
        authResponseDto.setRefreshToken(jwtService.generateRefreshToken(email, newTokenId));
        account.setLastRefreshTokenId(newTokenId);
        accountRepository.save(account);

        response.setStatusCode(HttpStatus.OK.value());
        response.setStatusMessage("Token refreshed");
        response.setData(authResponseDto);
        return response;
    }

    private Account verifyAccount(LoginRequestDto loginRequestDto){
        Account account = accountRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(()
                -> new ResourceNotFound("Email not found!"));
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), account.getPassword()))
            throw new BadCredentialsException("Invalid email or password");
        return account;
    }
}
