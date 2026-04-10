package com.domain.chat_service.app.message.dto;

import com.domain.chat_service.app.message.enums.Status;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AcknowledgeableMessage {
    private Long id;
    private UUID uuid;
    private UUID roomId;
    private Status status;
    private String senderEmail;
    private LocalDateTime createdAt;
}
