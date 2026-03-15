package com.domain.chat_service.app.presence.event.dto;

import com.domain.chat_service.app.presence.event.enums.Status;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PresenceDto {
    private String username;
    private Status status;
    private String lastSeen;
}
