package com.domain.chat_service.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig {
    @Bean
    public AuthorizationManager<Message<?>> authorizationManager(
            MessageMatcherDelegatingAuthorizationManager.Builder messages
    ) {
        messages.simpTypeMatchers(SimpMessageType.CONNECT).permitAll();
        messages.simpTypeMatchers(SimpMessageType.SUBSCRIBE).permitAll();
        messages.anyMessage().permitAll();
        return messages.build();
    }
}
