package com.messenger.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChatMessagesListDto {
    private List<ChatMessageDto> messages;
}
