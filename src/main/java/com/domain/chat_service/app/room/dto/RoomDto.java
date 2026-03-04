package com.domain.chat_service.app.room.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private UUID referenceNumber;
}
