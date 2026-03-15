package com.domain.chat_service.app.presence.event.dto;

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
}
