package com.domain.chat_service.app.room.dto;

import com.domain.chat_service.app.message.dto.MessageDto;
import com.domain.chat_service.app.room.enums.Type;
import com.domain.chat_service.app.user.dto.UserInfo;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private Long id;
    private UUID referenceNumber;
    private Type type;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<UserInfo> participants;
    private MessageDto lastMessage;
}
