package com.messenger.authorization.microservice.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TokenDto {
    private UUID Id;
    private String token;
}
