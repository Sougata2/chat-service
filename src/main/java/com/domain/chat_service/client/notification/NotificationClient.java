package com.domain.chat_service.client.notification;

import com.domain.chat_service.app.notification.dto.NotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "notification-service")
public interface NotificationClient {
    @PostMapping("/web-push/notify")
    void notify(@RequestHeader("X-Username") String username, @RequestHeader("X-Role") String role, @RequestBody NotificationDto dto);
}
