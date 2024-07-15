package com.messenger.services;

import com.messenger.dtos.*;
import com.messenger.exceptions.AppError;
import com.messenger.store.entites.Chat;
import com.messenger.store.entites.Message;
import com.messenger.store.entites.User;
import com.messenger.store.repositories.ChatRepository;
import com.messenger.store.repositories.MessageRepository;
import com.messenger.store.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class MessageService {
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    public ResponseEntity<?> getHistoryMessages(String token, Long idChat) {
        try {
            User user = userRepository.findById(tokenService.getUserId(token)).orElseThrow(
                    () -> new EntityNotFoundException("Пользователь с таким id  не найден"));
            Chat chat = chatRepository.findById(idChat).orElseThrow(
                    () -> new EntityNotFoundException("Чат с таким id  не найден"));
            List<ChatMessageDto> messages = new ArrayList<>();
            for (Message message : chat.getMessages()) {
                if (message.getIsDeleted())
                    continue;
                messages.add(new ChatMessageDto(message.getId(), message.getMessage(), message.getAuthor().getId(),
                        message.getAuthor().getUsername(), message.getUpdatedAt()));
            }
            return ResponseEntity.ok(new ChatMessagesListDto(messages));
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.debug(e.toString());
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Произошла ошибка при загрузке истории чата"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> addNewMessage(String token, Long idChat, RequestNewMessageDto newMessageDto) {
        try {
            User user = userRepository.findById(tokenService.getUserId(token)).orElseThrow(
                    () -> new EntityNotFoundException("Пользователь с таким id  не найден"));
            Chat chat = chatRepository.findById(idChat).orElseThrow(
                    () -> new EntityNotFoundException("Чат с таким id  не найден"));
            Message message = new Message(chat, user, newMessageDto.getMessage());
            messageRepository.save(message);
            ChatMessageDto messageDto = new ChatMessageDto(message.getId(), message.getMessage(), user.getId(),
                    user.getUsername(), message.getUpdatedAt());
            return ResponseEntity.ok(messageDto);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.debug(e.toString());
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Произошла ошибка при создании сообщения"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> updateMessage(String token, Long idChat, RequestUpdateMessageDto updateMessageDto) {
        try {
            User user = userRepository.findById(tokenService.getUserId(token)).orElseThrow(
                    () -> new EntityNotFoundException("Пользователь с таким id  не найден"));
            Chat chat = chatRepository.findByIdAndCreator(idChat, user).orElseThrow(
                    () -> new EntityNotFoundException("Чат с таким id  не найден"));
            Message message = messageRepository.findByIdAndChatIdAndUserId(
                    updateMessageDto.getId(), chat, user).orElseThrow(
                    () -> new EntityNotFoundException("Сообщение с такими критериями  не найдено"));
            if (message.getIsDeleted())
                throw new EntityNotFoundException("Сообщение не найдено");
            message.setMessage(updateMessageDto.getMessage());
            messageRepository.save(message);
            ResponseMessageDto messageDto = new ResponseMessageDto(message.getId(), chat.getId(), user.getId(),
                    message.getMessage(), message.getUpdatedAt(), message.getIsSeen());
            return ResponseEntity.ok(messageDto);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.debug(e.toString());
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Произошла ошибка при изменении сообщения"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteMessage(String token, Long idChat, RequestDeleteMessageDto deleteMessageDto) {
        try {
            User user = userRepository.findById(tokenService.getUserId(token)).orElseThrow(
                    () -> new EntityNotFoundException("Пользователь с таким id  не найден"));
            Chat chat = chatRepository.findByIdAndCreator(idChat, user).orElseThrow(
                    () -> new EntityNotFoundException("Чат с таким id  не найден"));
            Message message = messageRepository.findByIdAndChatIdAndUserId(
                    deleteMessageDto.getId(), chat, user).orElseThrow(
                    () -> new EntityNotFoundException("Сообщение с такими критериями  не найдено"));
            message.setIsDeleted(true);
            messageRepository.save(message);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.debug(e.toString());
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Произошла ошибка при изменении сообщения"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
