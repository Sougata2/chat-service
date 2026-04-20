package com.domain.chat_service.app.notification.service;


import com.domain.chat_service.app.message.dto.MessageDto;

import java.security.Principal;

public interface NotificationService {
    void sendNotification(MessageDto dto, Principal principal);
}
