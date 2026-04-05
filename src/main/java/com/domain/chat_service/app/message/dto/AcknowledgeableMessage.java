package com.domain.chat_service.app.message.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AcknowledgeableMessage {
    private Long id;
    private UUID uuid;
    private String senderEmail;
}
