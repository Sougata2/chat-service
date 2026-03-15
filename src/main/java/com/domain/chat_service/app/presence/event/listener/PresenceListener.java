package com.domain.chat_service.app.presence.event.listener;

import com.domain.chat_service.app.presence.event.dto.PresenceDto;
import com.domain.chat_service.app.presence.event.enums.Status;
import com.domain.chat_service.app.presence.services.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class PresenceListener {
    private final PresenceService service;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleConnect(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        Principal user = accessor.getUser();

        if (user != null) {
            String username = user.getName();
            service.userOnline(username, accessor.getSessionId());
            messagingTemplate.convertAndSend("/topic/presence",
                    PresenceDto.builder()
                            .username(username)
                            .status(Status.ONLINE)
                            .build()
            );
        }
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = accessor.getUser();
        if (user != null) {
            String username = user.getName();
            String lastSeen = service.userOffline(accessor.getSessionId());

            messagingTemplate.convertAndSend("/topic/presence",
                    PresenceDto.builder()
                            .username(username)
                            .lastSeen(lastSeen)
                            .status(Status.OFFLINE)
                            .build()
            );
        }
    }

}
