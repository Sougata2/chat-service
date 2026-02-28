package com.domain.chat_service.app.chat.controller;

import com.domain.chat_service.app.message.dto.GroupMessage;
import com.domain.chat_service.app.message.dto.PrivateMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate template;

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public String send(String message, Principal principal) {
        return principal.getName() + " : " + message;
    }

    @MessageMapping("/private.send")
    public void sendPrivate(PrivateMessage message, Principal principal) {
        message.setSender(principal.getName());
        template.convertAndSendToUser(message.getRecipient(), "/queue/messages", message);
    }

    @MessageMapping("/group.send")
    public void sendGroup(GroupMessage message, Principal principal) {
        message.setSender(principal.getName());
        template.convertAndSend("/topic/room/%s".formatted(message.getReferenceNumber()), message);
    }
}
