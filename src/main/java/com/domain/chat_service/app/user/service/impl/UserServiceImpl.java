package com.domain.chat_service.app.user.service.impl;

import com.domain.chat_service.app.user.Auth;
import com.domain.chat_service.app.user.entity.UserEntity;
import com.domain.chat_service.app.user.repository.UserRepository;
import com.domain.chat_service.app.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public Auth getAuth(String username) {
        UserEntity user = repository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("user %s is not found".formatted(username)));
        return new Auth(user.getEmail(), user.getCurrentRole());
    }
}
