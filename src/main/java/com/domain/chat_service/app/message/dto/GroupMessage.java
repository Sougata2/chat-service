package com.domain.chat_service.app.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMessage {
    private MessageDto message;
    private UUID referenceNumber;
}
