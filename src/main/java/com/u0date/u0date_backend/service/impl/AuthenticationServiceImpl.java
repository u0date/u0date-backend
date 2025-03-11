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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final JWTService jwtService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountDetailsService accountDetailsService;

    @Override
    public DefaultApiResponse<AuthResponseDto> login(@Valid LoginRequestDto loginRequestDto, String deviceId) {
        AuthResponseDto authResponseDto = new AuthResponseDto();
        String tokenId = UUID.randomUUID().toString();
        Account account = verifyAccount(loginRequestDto);
        authResponseDto.setAccessToken(jwtService.generateToken(account.getEmail(), tokenId));
        authResponseDto.setRefreshToken(jwtService.generateRefreshToken(account.getEmail(), tokenId));

        account.updateRefreshToken(deviceId, tokenId);
        accountRepository.save(account);
        return new DefaultApiResponse<>(HttpStatus.OK.value(), "Login Successful", authResponseDto);
    }

    @Override
    public DefaultApiResponse<AuthResponseDto> refreshToken(RefreshTokenDto refreshTokenDto, String deviceId) {
        AuthResponseDto authResponseDto = new AuthResponseDto();
        String email = jwtService.extractEmail(refreshTokenDto.getRefreshToken());

        if (email == null) {
            log.warn("[Authentication] Failed token refresh attempt - Email extraction failed");
            throw new JwtException("An error occurred refreshing access token");
        }

        if (!jwtService.isRefreshTokenValid(refreshTokenDto.getRefreshToken(), accountDetailsService.loadUserByUsername(email))) {
            log.warn("[Authentication] Failed token refresh attempt - Invalid refresh token for email: {}", email);
            throw new JwtException("Invalid refresh token");
        }

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("[Authentication] Failed token refresh attempt - Account not found for email: {}", email);
                    return new ResourceNotFound("Invalid Email");
                });

        String tokenId = jwtService.extractTokenId(refreshTokenDto.getRefreshToken());

        if (!account.isValidRefreshToken(deviceId, tokenId)) {
            log.warn("[Authentication] Failed token refresh attempt - Invalid refresh token for device: {}", deviceId);
            throw new ResourceNotFound("Invalid refresh token for this device");
        }

        String newTokenId = UUID.randomUUID().toString();
        authResponseDto.setAccessToken(jwtService.generateToken(email, newTokenId));
        authResponseDto.setRefreshToken(jwtService.generateRefreshToken(email, newTokenId));

        account.updateRefreshToken(deviceId, newTokenId);
        accountRepository.save(account);

        log.info("[Authentication] Token refreshed successfully for email: {}", email);
        return new DefaultApiResponse<>(HttpStatus.OK.value(), "Token refreshed", authResponseDto);
    }

    private Account verifyAccount(LoginRequestDto loginRequestDto){
        Account account = accountRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFound("[Authentication] Account not found for email: " + loginRequestDto.getEmail()));
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), account.getPassword())){
            log.warn("[Authentication] Failed login attempt for email: {}", loginRequestDto.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }
        return account;
    }
}
