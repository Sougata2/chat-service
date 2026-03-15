package com.domain.chat_service.app.presence.controller;

import com.domain.chat_service.app.presence.services.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/presence")
public class PresenceController {
    private final PresenceService service;

    @GetMapping("/online-users")
    public ResponseEntity<List<String>> getOnlineUsers() {
        return ResponseEntity.ok(service.getOnlineUsers());
    }
}
