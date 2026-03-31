package com.domain.chat_service.app.chat.controller;

import com.domain.chat_service.app.file.dto.FileDto;
import com.domain.chat_service.app.message.dto.AcknowledgementDto;
import com.domain.chat_service.app.message.dto.GroupMessage;
import com.domain.chat_service.app.message.dto.OutGoingMessage;
import com.domain.chat_service.app.message.dto.PrivateMessage;
import com.domain.chat_service.app.message.enums.Media;
import com.domain.chat_service.app.room.dto.RoomDto;
import com.domain.chat_service.app.user.Auth;
import com.domain.chat_service.app.user.dto.UserInfo;
import com.domain.chat_service.app.user.service.UserService;
import com.domain.chat_service.client.message.MessageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

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
//        MessageDto sent = messageClient.saveMessage(auth.getUsername(), auth.getRole(), message.getMessage());
        if (message.getMessage().getMedia().equals(Media.TEXT)) {
            OutGoingMessage outGoingMessage = OutGoingMessage.builder()
                    .message(message.getMessage())
                    .build();
            template.convertAndSendToUser(message.getRecipient(), "/queue/messages", outGoingMessage);
            return;
        }
        List<FileDto> files = messageClient.findByMessage(auth.getUsername(), auth.getRole(), message.getMessage().getUuid());
        template.convertAndSendToUser(
                message.getRecipient(), "/queue/messages",
                OutGoingMessage.builder()
                        .message(message.getMessage())
                        .files(files)
                        .build()
        );
    }

    @MessageMapping("/group.post")
    public void createGroup(RoomDto room, Principal principal) {
        for (UserInfo participant : room.getParticipants()) {
            if (!participant.getEmail().equals(principal.getName())) {
                template.convertAndSendToUser(participant.getEmail(), "/queue/rooms", room);
            }
        }
    }

    @MessageMapping("/group.send")
    public void sendGroup(GroupMessage message, Principal principal) {
        Auth auth = userService.getAuth(principal.getName());
//        MessageDto sent = messageClient.saveMessage(auth.getUsername(), auth.getRole(), message.getMessage());
        if (message.getMessage().getMedia().equals(Media.TEXT)) {
            OutGoingMessage outGoingMessage = OutGoingMessage.builder()
                    .message(message.getMessage())
                    .build();
            template.convertAndSend("/topic/room/%s".formatted(message.getReferenceNumber()), outGoingMessage);
            return;
        }
        List<FileDto> files = messageClient.findByMessage(auth.getUsername(), auth.getRole(), message.getMessage().getUuid());
        template.convertAndSend(
                "/topic/room/%s".formatted(message.getReferenceNumber()),
                OutGoingMessage.builder()
                        .message(message.getMessage())
                        .files(files)
                        .build()
        );
    }

    @MessageMapping("/post.acknowledge")
    public void acknowledge(AcknowledgementDto dto, Principal principal) {
        Auth auth = userService.getAuth(principal.getName());
        messageClient.acknowledge(auth.getUsername(), auth.getRole(), dto);

    }
}
