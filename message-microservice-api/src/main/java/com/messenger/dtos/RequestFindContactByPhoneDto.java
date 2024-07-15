package com.messenger.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestFindContactByPhoneDto {
    private String phone;
}
