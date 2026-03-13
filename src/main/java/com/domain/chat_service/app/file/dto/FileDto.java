package com.domain.chat_service.app.file.dto;

import com.domain.chat_service.app.file.enums.Status;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {
    private Long id;
    private String url;
    private String originalName;
    private Long size;
    private String mimeType;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID messageUUID;
}
