package com.domain.chat_service.app.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController {
    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public String send(String message, Principal principal) {
        return principal.getName() + " : " + message;
    }
}
