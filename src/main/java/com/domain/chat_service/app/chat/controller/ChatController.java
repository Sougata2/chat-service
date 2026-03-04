package com.domain.chat_service.app.chat.controller;

import com.domain.chat_service.app.message.dto.GroupMessage;
import com.domain.chat_service.app.message.dto.MessageDto;
import com.domain.chat_service.app.message.dto.PrivateMessage;
import com.domain.chat_service.app.user.Auth;
import com.domain.chat_service.app.user.service.UserService;
import com.domain.chat_service.client.message.MessageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate template;
    private final MessageClient messageClient;
    private final UserService userService;

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public String send(String message, Principal principal) {
        return principal.getName() + " : " + message;
    }

    @MessageMapping("/private.send")
    public void sendPrivate(PrivateMessage message, Principal principal) {
        Auth auth = userService.getAuth(principal.getName());
        MessageDto sent = messageClient.saveMessage(auth.getUsername(), auth.getRole(), message.getMessage());
        template.convertAndSendToUser(message.getRecipient(), "/queue/messages", sent);
        template.convertAndSendToUser(principal.getName(), "/queue/messages", sent);
    }

    @MessageMapping("/group.send")
    public void sendGroup(GroupMessage message, Principal principal) {
        message.setSender(principal.getName());
        template.convertAndSend("/topic/room/%s".formatted(message.getReferenceNumber()), message);
    }

    private String getAuthorization(Principal principal) {
        System.out.println(((Authentication) principal).getCredentials());
        return "Bearer " + ((Authentication) principal).getCredentials().toString();
    }
}
