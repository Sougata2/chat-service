package com.domain.chat_service.web_socket.component;

import com.domain.chat_service.app.user.entity.UserEntity;
import com.domain.chat_service.app.user.repository.UserRepository;
import com.domain.chat_service.jwt.service.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        // If already authenticated, reuse it
        if (accessor.getUser() != null) {
            SecurityContextHolder.getContext()
                    .setAuthentication((Authentication) accessor.getUser());
            return message;
        }

        // Authenticate only on CONNECT
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);
            String username = jwtService.getUsername(token);

            UserEntity user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            token,
                            AuthorityUtils.commaSeparatedStringToAuthorityList(
                                    user.getCurrentRole()
                            )
                    );

            accessor.setUser(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return message;
    }
}