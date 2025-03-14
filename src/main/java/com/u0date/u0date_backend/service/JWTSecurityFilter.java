package com.u0date.u0date_backend.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTSecurityFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final ApplicationContext applicationContext;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(@NonNull  HttpServletRequest request, @NonNull  HttpServletResponse response, @NonNull  FilterChain filterChain){
        try{
            String authHeader = request.getHeader("Authorization");
            String deviceId = request.getParameter("deviceId");
            String email = null;
            String jwtToken = null;

            if (deviceId.isEmpty()) {
                log.warn("[Device Validation] Device ID is missing in the request");
                throw new RuntimeException("Device ID is required but missing");
            }

            if(authHeader != null && authHeader.startsWith("Bearer ")){
                jwtToken = authHeader.substring(7);
                email = jwtService.extractEmail(jwtToken);
            }

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = applicationContext.getBean(AccountDetailsService.class).loadUserByUsername(email);
                if(jwtService.validateToken(jwtToken, userDetails, deviceId)){
                    UsernamePasswordAuthenticationToken token =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception exception){
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
