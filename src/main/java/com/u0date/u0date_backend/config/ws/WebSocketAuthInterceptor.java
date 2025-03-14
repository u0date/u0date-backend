package com.u0date.u0date_backend.config.ws;

import com.u0date.u0date_backend.entity.AccountPrincipal;
import com.u0date.u0date_backend.service.AccountDetailsService;
import com.u0date.u0date_backend.service.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthInterceptor implements HandshakeInterceptor {
    private final AccountDetailsService accountDetailsService;
    private final JWTService jwtService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            String query = servletRequest.getServletRequest().getQueryString();

            if (query != null && query.contains("token=")) {
                String jwtToken = query.split("token=")[1].split("&")[0];
                String deviceId = query.contains("deviceId=") ? query.split("deviceId=")[1].split("&")[0] : null;

                if(deviceId == null)
                    throw new RuntimeException("Device ID missing");

                String email = jwtService.extractEmail(jwtToken);
                if (email != null) {
                    AccountPrincipal accountPrincipal = (AccountPrincipal) accountDetailsService.loadUserByUsername(email);;
                    if (jwtService.validateToken(jwtToken, accountPrincipal, deviceId))
                        attributes.put("principal", accountPrincipal);
                }
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {}
}
