package com.messenger.controllers;

import com.messenger.dtos.AddUserDto;
import com.messenger.dtos.UserOnlineStatusDto;
import com.messenger.exceptions.AppError;
import com.messenger.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messenger/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @CrossOrigin(origins = "*")
    @GetMapping("/")
    public ResponseEntity<?> showUsers(){
        return userService.showusers();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/add")
    public ResponseEntity<?> addUserListener(
            @RequestHeader("Authorization") String token,
            @RequestBody AddUserDto addUserDto) {
        return userService.addNewUser(addUserDto);
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/online-stat")
    public ResponseEntity<?> onlineStatUserListener(
            @RequestHeader("Authorization") String token,
            @RequestBody UserOnlineStatusDto userOnlineStatusDto) {
        return userService.changeOnlineStatus(userOnlineStatusDto);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Неверная строка запроса"), HttpStatus.BAD_REQUEST);
        }
        return userService.deleteUserById(id);
    }
}

