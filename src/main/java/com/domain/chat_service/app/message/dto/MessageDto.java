package com.domain.chat_service.app.message.dto;

import com.domain.chat_service.app.message.enums.Media;
import com.domain.chat_service.app.message.enums.Status;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long id;
    private String message;
    private UUID uuid;
    private Status status;
    private Media media;
    private UUID roomRef;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long senderId;
    private String senderEmail;
    private String senderFirstName;
    private String senderLastName;
}
