package com.domain.chat_service.app.presence.controller;

import com.domain.chat_service.app.presence.enums.Status;
import com.domain.chat_service.app.presence.event.dto.TypingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Duration;

@Controller
@RequiredArgsConstructor
public class TypingController {
    private final SimpMessagingTemplate messagingTemplate;
    private final StringRedisTemplate redisTemplate;
    private static final String TYPING = "typing";

    @MessageMapping("/chat.typing")
    public void typing(TypingDto dto) {
        String key = "%s:%s:%s".formatted(TYPING, dto.getRoomRef(), dto.getUsername());
        Boolean exists = redisTemplate.hasKey(key);
        if (exists && dto.getStatus().equals(Status.STOP)) {
            redisTemplate.delete(key);
            messagingTemplate.convertAndSend("/topic/typing/" + dto.getRoomRef(), dto);
            return;
        }
        redisTemplate.opsForValue().set(key, "1", Duration.ofSeconds(3));
        messagingTemplate.convertAndSend("/topic/typing/" + dto.getRoomRef(), dto);
    }
}
