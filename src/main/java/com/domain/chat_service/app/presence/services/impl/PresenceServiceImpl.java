package com.domain.chat_service.app.presence.services.impl;

import com.domain.chat_service.app.presence.services.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PresenceServiceImpl implements PresenceService {
    private final StringRedisTemplate redisTemplate;
    private static final String ONLINE_USERS = "online_users";
    private static final String USER_SESSIONS = "user_sessions";
    private static final String SOCKET_USER = "socket_user";
    private static final String USER_LAST_SEEN = "user_last_seen";

    @Override
    public void userOnline(String username, String sessionId) {
        Long sessions = redisTemplate.opsForValue().increment(USER_SESSIONS + ":" + username);
        redisTemplate.opsForValue().set(SOCKET_USER + ":" + sessionId, username, Duration.ofHours(1));
        if (sessions != null && sessions == 1) {
            redisTemplate.opsForSet().add(ONLINE_USERS, username);
        }
    }

    @Override
    public String userOffline(String sessionId) {
        String lastSeen;
        String username = redisTemplate.opsForValue().get(SOCKET_USER + ":" + sessionId);

        if (username == null) return null;

        Long sessions = redisTemplate.opsForValue().decrement(USER_SESSIONS + ":" + username);

        if (sessions != null && sessions <= 0) {
            redisTemplate.opsForSet().remove(ONLINE_USERS, username);
            lastSeen = String.valueOf(System.currentTimeMillis());
            redisTemplate.opsForValue().set(USER_LAST_SEEN + ":" + username, lastSeen);
            redisTemplate.delete(USER_SESSIONS + ":" + username);
            return lastSeen;
        }

        redisTemplate.delete(SOCKET_USER + ":" + sessionId);
        return null;
    }

    @Override
    public boolean isOnline(String username) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(ONLINE_USERS, username));
    }

    @Override
    public List<String> getOnlineUsers() {
        Set<String> onlineUsers = redisTemplate.opsForSet().members(ONLINE_USERS);
        if (onlineUsers == null) return List.of();
        return onlineUsers.stream().toList();
    }
}
