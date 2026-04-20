package com.domain.chat_service.client.message;

import com.domain.chat_service.app.file.dto.FileDto;
import com.domain.chat_service.app.message.dto.AcknowledgementDto;
import com.domain.chat_service.app.message.dto.MessageDto;
import com.domain.chat_service.app.room.dto.RoomDto;
import com.domain.chat_service.app.user.dto.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "message-service")
public interface MessageClient {
    @PostMapping("/messages")
    MessageDto saveMessage(@RequestHeader("X-Username") String username, @RequestHeader("X-Role") String role, @RequestBody MessageDto message);

    @GetMapping("/files/message/{uuid}")
    List<FileDto> findByMessage(@RequestHeader("X-Username") String username, @RequestHeader("X-Role") String role, @PathVariable UUID uuid);

    @PostMapping("/users/update-last-seen")
    void updateLastSeen(@RequestHeader("X-Username") String username, @RequestHeader("X-Role") String role, @RequestBody UserInfo userInfo);

    @PostMapping("/messages/acknowledge")
    Map<String, List<MessageDto>> acknowledge(@RequestHeader("X-Username") String username, @RequestHeader("X-Role") String role, @RequestBody AcknowledgementDto dto);

    @GetMapping("/rooms/reference/{number}")
    RoomDto getRoomInfo(@RequestHeader("X-Username") String username, @RequestHeader("X-Role") String role, @PathVariable UUID number);
}
