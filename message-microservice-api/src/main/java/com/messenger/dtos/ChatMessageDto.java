package com.messenger.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ChatMessageDto {
    private Long id;
    private String body;
    private Long idAuthor;
    private String author;
    private Instant time;
}
