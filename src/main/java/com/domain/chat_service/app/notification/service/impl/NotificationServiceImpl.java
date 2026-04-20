package com.domain.chat_service.app.notification.service.impl;

import com.domain.chat_service.app.message.dto.MessageDto;
import com.domain.chat_service.app.message.enums.Media;
import com.domain.chat_service.app.notification.dto.NotificationDto;
import com.domain.chat_service.app.notification.service.NotificationService;
import com.domain.chat_service.app.room.dto.RoomDto;
import com.domain.chat_service.app.room.enums.Type;
import com.domain.chat_service.app.user.Auth;
import com.domain.chat_service.app.user.dto.UserInfo;
import com.domain.chat_service.app.user.service.UserService;
import com.domain.chat_service.client.message.MessageClient;
import com.domain.chat_service.client.notification.NotificationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationClient notificationClient;
    private final MessageClient messageClient;
    private final UserService userService;

    @Override
    public void sendNotification(MessageDto dto, Principal principal) {
        Auth auth = userService.getAuth(dto.getSenderEmail());
        RoomDto room = messageClient.getRoomInfo(auth.getUsername(), auth.getRole(), dto.getRoomRef());

        List<String> emails = room.getParticipants().stream()
                .map(UserInfo::getEmail)
                .filter(email -> !email.equals(principal.getName()))
                .toList();

        String title = room.getType() == Type.GROUP ?
                room.getName() :
                "%s %s".formatted(dto.getSenderFirstName(), dto.getSenderLastName());

        String bodyPrefix = room.getType() == Type.GROUP ? dto.getSenderFirstName() + ":" : "";
        String body = getBody(dto);
        NotificationDto notificationDto = NotificationDto.builder()
                .title(title)
                .body(bodyPrefix + body)
                .emails(emails)
                .build();
        notificationClient.notify(auth.getUsername(), auth.getRole(), notificationDto);
    }

    private String getBody(MessageDto dto) {
        String body = "";
        if (dto.getMedia().equals(Media.TEXT)) {
            body = dto.getMessage().length() > 20 ?
                    dto.getMessage().substring(0, 20) + "..." :
                    dto.getMessage();

        } else if (dto.getMedia().equals(Media.IMAGE)) {
            body = "📷";
        } else if (dto.getMedia().equals(Media.VIDEO)) {
            body = "🎥";
        } else if (dto.getMedia().equals(Media.DOCUMENT)) {
            body = "📄";
        } else if (dto.getMedia().equals(Media.AUDIO)) {
            body = "🔉";
        }
        return body;
    }
}
