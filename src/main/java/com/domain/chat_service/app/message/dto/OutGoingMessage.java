package com.domain.chat_service.app.message.dto;

import com.domain.chat_service.app.file.dto.FileDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OutGoingMessage {
    private MessageDto message;
    private List<FileDto> files;
}
