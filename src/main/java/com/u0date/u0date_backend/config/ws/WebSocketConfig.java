package com.u0date.u0date_backend.config.ws;

import com.u0date.u0date_backend.service.AccountDetailsService;
import com.u0date.u0date_backend.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
    private final AccountDetailsService accountDetailsService;
    private final JWTService jwtService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:5173", "http://localhost:5500", "http://127.0.0.1:5500")
                .addInterceptors(new WebSocketAuthInterceptor(accountDetailsService, jwtService))
                .withSockJS();
    }
}
