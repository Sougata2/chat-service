package com.domain.chat_service.app.presence.services.impl;

import com.domain.chat_service.app.presence.event.dto.PresenceDto;
import com.domain.chat_service.app.presence.event.enums.Status;
import com.domain.chat_service.app.presence.services.PresenceService;
import com.domain.chat_service.app.user.Auth;
import com.domain.chat_service.app.user.dto.UserInfo;
import com.domain.chat_service.app.user.service.UserService;
import com.domain.chat_service.client.message.MessageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Duration;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PresenceServiceImpl implements PresenceService {
    private final MessageClient messageClient;
    private final UserService userService;
    private final StringRedisTemplate redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private static final String ONLINE_USERS = "online_users";
    private static final String USER_SESSIONS = "user_sessions";
    private static final String SOCKET_USER = "socket_user";
    private static final String USER_LAST_SEEN = "user_last_seen";

    @Override
    public void userOnline(String username, String sessionId) {
        redisTemplate.opsForSet().add(USER_SESSIONS + ":" + username, sessionId);
        redisTemplate.opsForValue().set(SOCKET_USER + ":" + sessionId, username, Duration.ofHours(1));

        Long size = redisTemplate.opsForSet().size(USER_SESSIONS + ":" + username);
        if (size != null && size > 0) {
            redisTemplate.opsForSet().add(ONLINE_USERS, username);
        }
    }

    @Override
    public void userOffline(String sessionId, Principal user) {
        String username = redisTemplate.opsForValue().get(SOCKET_USER + ":" + sessionId);
        if (username == null) return;
        redisTemplate.opsForSet().remove(USER_SESSIONS + ":" + username, sessionId);
        redisTemplate.delete(SOCKET_USER + ":" + sessionId);
        delayedOfflineCheck(username);
    }

    @Override
    public boolean isOnline(String username) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(ONLINE_USERS, username));
    }

    @Override
    public List<PresenceDto> getOnlineUsers() {
        Set<String> onlineUsers = redisTemplate.opsForSet().members(ONLINE_USERS);
        if (onlineUsers == null) return List.of();
        return onlineUsers.stream().map(
                user ->
                        PresenceDto.builder()
                                .username(user)
                                .status(Status.ONLINE)
                                .build()
        ).toList();
    }

    @Async
    @Override
    public void updateLastSeenAsync(String username, Long lastSeen) {
        Auth auth = userService.getAuth(username);

        messageClient.updateLastSeen(
                auth.getUsername(),
                auth.getRole(),
                UserInfo.builder().lastSeen(lastSeen).build()
        );
    }

    @Async
    public void delayedOfflineCheck(String username) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Long size = redisTemplate.opsForSet().size(USER_SESSIONS + ":" + username);
        if (size == null || size == 0) {
            redisTemplate.opsForSet().remove(ONLINE_USERS, username);

            Long lastSeen = System.currentTimeMillis();
            updateLastSeenAsync(username, lastSeen);
            redisTemplate.opsForValue().set(USER_LAST_SEEN + ":" + username, String.valueOf(lastSeen), Duration.ofHours(1));

            messagingTemplate.convertAndSend("/topic/presence",
                    PresenceDto.builder()
                            .username(username)
                            .lastSeen(String.valueOf(lastSeen))
                            .status(Status.OFFLINE)
                            .build()
            );
        }
    }
}
