package com.domain.chat_service.app.presence.services;

import com.domain.chat_service.app.presence.event.dto.PresenceDto;

import java.util.List;

public interface PresenceService {
    void userOnline(String username, String sessionId);

    String userOffline(String sessionId);

    boolean isOnline(String username);

    List<PresenceDto> getOnlineUsers();
}
