package com.domain.chat_service.app.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Long lastSeen;
}
