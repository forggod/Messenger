package com.messenger.dtos;

import lombok.Data;

import java.time.Instant;

@Data
public class AddUserDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private Instant lastLogin;
    private Instant createdAt;
}
