package com.messenger.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResponseContactsDto {
    private List<ResponseContactUserDto> users;
}
