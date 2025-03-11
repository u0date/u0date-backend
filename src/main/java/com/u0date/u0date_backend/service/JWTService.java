package com.u0date.u0date_backend.service;

import com.u0date.u0date_backend.entity.Account;
import com.u0date.u0date_backend.exception.ResourceNotFound;
import com.u0date.u0date_backend.repository.AccountRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class JWTService {
    @Value("${spring.security.jwt.secret_key}")
    private String SECRET_KEY;

    @Value("${spring.security.jwt.token_expiration}")
    private int tokenExpiration;

    @Value("${spring.security.jwt.refresh_token_expiration}")
    private int refreshTokenExpiration;

    private final AccountRepository accountRepository;

    public String generateToken(String email, String tokenId) {
        Map<String, String> claims = new HashMap<>();
        claims.put("tokenId", tokenId);

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getKey())
                .compact();
    }

    public String generateRefreshToken(String email, String tokenId) {
        Map<String, String> claims = new HashMap<>();
        claims.put("tokenId", tokenId);

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getKey())
                .compact();
    }

    public String extractEmail(String jwtToken) {
        return extractClaims(jwtToken, Claims::getSubject);
    }

    public String extractTokenId(String jwtToken) {
        return extractClaims(jwtToken, claims -> claims.get("tokenId", String.class));
    }

    public boolean validateToken(String jwtToken, UserDetails userDetails, String deviceId) {
        final String email = extractEmail(jwtToken);
        final String tokenId = extractTokenId(jwtToken);

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("Account not found for email: " + email));

        return email.equals(userDetails.getUsername())
                && !isTokenExpired(jwtToken)
                && account.isValidRefreshToken(deviceId, tokenId);
    }

    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        String subject = extractClaims(token, Claims::getSubject);
        return (subject.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private <T> T extractClaims(String jwtToken, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(
                Jwts.parser()
                        .verifyWith(getKey())
                        .build()
                        .parseSignedClaims(jwtToken)
                        .getPayload());
    }

    private boolean isTokenExpired(String jwtToken){
        return extractClaims(jwtToken, Claims::getExpiration).before(new Date());
    }

    private SecretKey getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
