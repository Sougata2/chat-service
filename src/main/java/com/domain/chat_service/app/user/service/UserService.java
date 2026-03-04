package com.domain.chat_service.app.user.service;

import com.domain.chat_service.app.user.Auth;

public interface UserService {
    Auth getAuth(String username);
}
