package com.messenger.authorization.microservice.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class AddUserDtoResponse {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private Instant lastLogin;
    private Instant createdAt;
}
