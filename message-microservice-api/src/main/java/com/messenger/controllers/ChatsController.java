package com.messenger.controllers;

import com.messenger.dtos.*;
import com.messenger.exceptions.AppError;
import com.messenger.services.ChatService;
import com.messenger.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messenger/chats")
@RequiredArgsConstructor
public class ChatsController {
    private final ChatService chatService;
    private final MessageService messageService;

    @CrossOrigin(origins = "*")
    @GetMapping("")
    public ResponseEntity<?> showChatsList(@RequestHeader("Authorization") String token) {
        return chatService.showChatsList(token);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/add")
    public ResponseEntity<?> createNewChat(
            @RequestHeader("Authorization") String token,
            @RequestBody RequestCreateNewChatDto createNewChatDto) {
        return chatService.createNewChat(token, createNewChatDto);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateChat(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long id,
            @RequestBody RequestUpdateChatDto updateChatDto) {
        if (id == null) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Неверная строка запроса"), HttpStatus.BAD_REQUEST);
        }
        return chatService.updateChat(token, id, updateChatDto);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteChat(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Неверная строка запроса"), HttpStatus.BAD_REQUEST);
        }
        return chatService.deleteChat(token, id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{id}/show/users")
    public ResponseEntity<?> showUsersInChat(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Неверная строка запроса"), HttpStatus.BAD_REQUEST);
        }
        return chatService.showUsersInChat(token, id);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/{id}/add/user")
    public ResponseEntity<?> addUsersInChat(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long id,
            @RequestBody RequestUsersInChatDto usersInChatDto) {
        if (id == null) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Неверная строка запроса"), HttpStatus.BAD_REQUEST);
        }
        return chatService.addUsersInChat(token, id, usersInChatDto);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/{id}/delete/user")
    public ResponseEntity<?> deleteUsersInChat(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long id,
            @RequestBody RequestUsersInChatDto usersInChatDto) {
        if (id == null) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Неверная строка запроса"), HttpStatus.BAD_REQUEST);
        }
        return chatService.deleteUsersFromChat(token, id, usersInChatDto);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{id}/history")
    public ResponseEntity<?> showChatHistory(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Неверная строка запроса"), HttpStatus.BAD_REQUEST);
        }
        return messageService.getHistoryMessages(token, id);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/{id-chat}/history/add-message")
    public ResponseEntity<?> addNewMessage(
            @RequestHeader("Authorization") String token,
            @PathVariable("id-chat") Long id,
            @RequestBody RequestNewMessageDto newMessageDto) {
        if (id == null) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Неверная строка запроса"), HttpStatus.BAD_REQUEST);
        }
        return messageService.addNewMessage(token, id, newMessageDto);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/{id-chat}/history/update-message")
    public ResponseEntity<?> updateMessageById(
            @RequestHeader("Authorization") String token,
            @PathVariable("id-chat") Long id,
            @RequestBody RequestUpdateMessageDto updateMessageDto) {
        if (id == null) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Неверная строка запроса"), HttpStatus.BAD_REQUEST);
        }
        return messageService.updateMessage(token, id, updateMessageDto);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/{id-chat}/history/delete-message")
    public ResponseEntity<?> deleteMessageById(
            @RequestHeader("Authorization") String token,
            @PathVariable("id-chat") Long idChat,
            @RequestBody RequestDeleteMessageDto deleteMessageDto) {
        if (idChat == null) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Неверная строка запроса"), HttpStatus.BAD_REQUEST);
        }
        return messageService.deleteMessage(token, idChat, deleteMessageDto);
    }
}