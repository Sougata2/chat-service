package com.domain.chat_service.app.presence.event.dto;

import com.domain.chat_service.app.presence.enums.Status;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TypingDto {
    private UUID roomRef;
    private String username;
    private Status status;
}
