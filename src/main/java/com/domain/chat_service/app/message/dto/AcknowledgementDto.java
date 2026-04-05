package com.domain.chat_service.app.message.dto;

import com.domain.chat_service.app.message.enums.Status;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AcknowledgementDto {
    private Map<UUID, List<AcknowledgeableMessage>> roomMessageMap;
    private Map<UUID, Status> statusMap;
}
