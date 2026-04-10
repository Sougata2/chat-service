package com.domain.chat_service.app.message.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AcknowledgementDto {
    private List<MessageDto> acknowledgeableMessages;
}
