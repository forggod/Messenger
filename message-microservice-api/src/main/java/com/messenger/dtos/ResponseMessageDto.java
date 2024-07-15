package com.messenger.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessageDto {
    private Long id;
    private Long id_chat;
    private Long id_creator;
    private String message;
    private Instant time;
    private Boolean isSeen;
}
