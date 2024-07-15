package com.messenger.authorization.microservice.api.dtos;

import lombok.Data;

@Data
public class LoginUserRequest {
    private String username;
    private String password;
}
