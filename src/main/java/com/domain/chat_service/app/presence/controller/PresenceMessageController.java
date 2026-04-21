package com.domain.chat_service.app.presence.controller;

import com.domain.chat_service.app.presence.dto.ActivityDto;
import com.domain.chat_service.app.presence.services.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class PresenceMessageController {
    private final PresenceService service;

    @MessageMapping("/heartbeat")
    public void heartbeat(Principal principal, @Header("simpSessionId") String sessionId) {
        service.refreshPresence(principal, sessionId);
    }

    @MessageMapping("/activity")
    public void activity(ActivityDto activityDto, Principal principal) {
        if (activityDto.isActive()) {
            service.registerActiveUser(principal.getName());
        }
    }
}
