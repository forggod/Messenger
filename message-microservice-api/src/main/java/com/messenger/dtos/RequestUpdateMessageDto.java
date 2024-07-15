package com.messenger.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestUpdateMessageDto {
    private Long id;
    private String message;
}
