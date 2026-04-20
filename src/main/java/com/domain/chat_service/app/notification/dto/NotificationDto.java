package com.domain.chat_service.app.notification.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private List<String> emails;
    private String title;
    private String body;
}

