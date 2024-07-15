package com.messenger.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseContactUserDto {
    private Long id;
    private String username;
}
