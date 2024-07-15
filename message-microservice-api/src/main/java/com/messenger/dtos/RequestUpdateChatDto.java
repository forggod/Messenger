package com.messenger.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestUpdateChatDto {
    private long id;
    private String name;
    private String description;
}
