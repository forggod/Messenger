package com.messenger.authorization.microservice.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenDto {
    private String refreshToken;
    private String username;
}
