package com.messenger.dtos;

import com.messenger.store.entites.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UsersDto {
    private List<User> usersList;
}
