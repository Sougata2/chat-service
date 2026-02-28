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

        messages
                // Framework-level messages
                .simpTypeMatchers(
                        SimpMessageType.CONNECT,
                        SimpMessageType.DISCONNECT,
                        SimpMessageType.HEARTBEAT
                ).permitAll()

                // Application SEND messages
                .simpDestMatchers("/app/**").authenticated()
                .simpDestMatchers("/user/**").authenticated()

                // Allow subscriptions
                .simpSubscribeDestMatchers("/topic/**", "/queue/**")
                .hasAnyRole("CHAT_USER", "ADMIN")

                .anyMessage().denyAll();

        return messages.build();
    }
}
