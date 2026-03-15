package com.domain.chat_service.app.presence.services;

import java.util.List;

public interface PresenceService {
    void userOnline(String username, String sessionId);

    String userOffline(String sessionId);

    boolean isOnline(String username);

    List<String> getOnlineUsers();
}
