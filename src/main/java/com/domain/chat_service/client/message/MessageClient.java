package com.domain.chat_service.client.message;

import com.domain.chat_service.app.message.dto.MessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "message-service")
public interface MessageClient {
    @PostMapping("/messages")
    MessageDto saveMessage(@RequestHeader("X-Username") String username, @RequestHeader("X-Role") String role, @RequestBody MessageDto message);
}
