package com.messenger.authorization.microservice.api.dtos;

import lombok.Data;

@Data
public class RegistrationUserDto {
    private String username;
    private String password;
    private String email;
    private String phone;
}
