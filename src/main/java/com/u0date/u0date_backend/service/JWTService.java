package com.u0date.u0date_backend.service;

import com.u0date.u0date_backend.entity.Account;
import com.u0date.u0date_backend.exception.ResourceNotFound;
import com.u0date.u0date_backend.repository.AccountRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTService {
    @Value("${spring.security.jwt.secret_key}")
    private String SECRET_KEY;
    private final AccountRepository accountRepository;

    public String generateToken(String email, String tokenId) {
        Map<String, String> claims = new HashMap<>();
        claims.put("tokenId", tokenId);

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))  // 15 minutes
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
                .expiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))  // 7 days
                .signWith(getKey())
                .compact();
    }

    public String extractEmail(String jwtToken) {
        return extractClaims(jwtToken, Claims::getSubject);
    }

    public String extractTokenId(String jwtToken) {
        return extractClaims(jwtToken, claims -> claims.get("tokenId", String.class));
    }

    public boolean validateToken(String jwtToken, UserDetails userDetails) {
        final String email = extractEmail(jwtToken);
        final String tokenId = extractTokenId(jwtToken); // Extract token ID

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("Account not found"));

        // Ensure token is not expired and matches the latest issued token ID
        return email.equals(userDetails.getUsername())
                && !isTokenExpired(jwtToken)
                && tokenId.equals(account.getLastRefreshTokenId());
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
