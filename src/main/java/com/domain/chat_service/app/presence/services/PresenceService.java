package com.domain.chat_service.app.presence.services;

import com.domain.chat_service.app.presence.event.dto.PresenceDto;

import java.security.Principal;
import java.util.List;

public interface PresenceService {
    void userOnline(String username, String sessionId);

    void userOffline(String sessionId, Principal user);

    boolean isOnline(String username);

    List<PresenceDto> getOnlineUsers();

    void updateLastSeenAsync(String username, Long lastSeen);

    void refreshPresence(Principal principal, String sessionId);

    void handleSessionExpiry(String sessionId);
}
