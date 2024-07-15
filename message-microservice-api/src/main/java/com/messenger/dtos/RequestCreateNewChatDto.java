package com.messenger.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestCreateNewChatDto {
    private String name;
    private String description;
}
