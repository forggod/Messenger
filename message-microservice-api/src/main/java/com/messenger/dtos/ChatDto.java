package com.messenger.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatDto {
    private Long id;
    private Long idAuthor;
    private String name;
    private String description;
}
