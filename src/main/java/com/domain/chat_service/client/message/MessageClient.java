package com.domain.chat_service.client.message;

import com.domain.chat_service.app.message.dto.PrivateMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "message-service")
public interface MessageClient {
    @PostMapping("/message/save")
    void saveMessage(@RequestHeader("Authorization") String authorization, @RequestBody PrivateMessage message);
}
