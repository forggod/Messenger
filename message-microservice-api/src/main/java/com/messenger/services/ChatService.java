package com.messenger.services;

import com.messenger.dtos.*;
import com.messenger.exceptions.AppError;
import com.messenger.store.entites.Chat;
import com.messenger.store.entites.UserInChats;
import com.messenger.store.entites.User;
import com.messenger.store.repositories.ChatRepository;
import com.messenger.store.repositories.UserInChatsRepository;
import com.messenger.store.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final UserInChatsRepository userInChatsRepository;

    public ResponseEntity<?> createNewChat(String token, RequestCreateNewChatDto createNewChatDto) {
        try {
            User user = userRepository.findById(tokenService.getUserId(token)).orElseThrow(
                    () -> new EntityNotFoundException("Пользователь с таким id  не найден"));
            Chat chat = chatRepository.save(
                    new Chat(createNewChatDto.getName(), createNewChatDto.getDescription(), user));
            UserInChats userInChats = userInChatsRepository.save(new UserInChats(chat, user));
            return ResponseEntity.ok(new ChatDto(chat.getId(), user.getId(), chat.getName(), chat.getDescription()));
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.debug(e.toString());
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Произошла ошибка при создании чата"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> showChatsList(String token) {
        try {
            User user = userRepository.findById(tokenService.getUserId(token)).orElseThrow(
                    () -> new EntityNotFoundException("Пользователь с таким id  не найден"));
            List<UserInChats> userInChatsList = userInChatsRepository.findByIdUser(user);
            List<Chat> chatList = new ArrayList<>();
            for (UserInChats userInChats : userInChatsList) {
                chatList.add(userInChats.getChat());
            }
            List<ChatDto> chatDtos = new ArrayList<>();
            for (Chat chat : chatList) {
                if (chat.getIsDeleted())
                    continue;
                chatDtos.add(new ChatDto(chat.getId(), chat.getCreator().getId(), chat.getName(), chat.getDescription()));
            }
            return ResponseEntity.ok(new ChatListDto(chatDtos));
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.debug(e.toString());
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Произошла ошибка при выводе списка чатов"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> updateChat(String token, Long id, RequestUpdateChatDto updateChatDto) {
        try {
            User user = userRepository.findById(tokenService.getUserId(token)).orElseThrow(
                    () -> new EntityNotFoundException("Пользователь с таким id  не найден"));
            Chat chat = chatRepository.findByIdAndCreator(updateChatDto.getId(), user).orElseThrow(
                    () -> new EntityNotFoundException("Чат с таким id  не найден"));
            if (chat.getIsDeleted())
                throw new EntityNotFoundException("Чат с таким id  не найден");
            chat.setName(updateChatDto.getName());
            chat.setDescription(updateChatDto.getDescription());
            chatRepository.save(chat);
            return ResponseEntity.ok(new ChatDto(chat.getId(), chat.getCreator().getId(), chat.getName(), chat.getDescription()));
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.debug(e.toString());
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Произошла ошибка при обновлении чата"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteChat(String token, Long id) {
        try {
            User user = userRepository.findById(tokenService.getUserId(token)).orElseThrow(
                    () -> new EntityNotFoundException("Пользователь с таким id  не найден"));
            Chat chat = chatRepository.findByIdAndCreator(id, user).orElseThrow(
                    () -> new EntityNotFoundException("Чат с таким id  не найден"));
            chat.setIsDeleted(true);
            chatRepository.save(chat);
            List<UserInChats> userInChatsList = chat.getUsersInChats();
            for (UserInChats userInChats: userInChatsList){
                userInChats.setIsDeleted(true);
                userInChatsRepository.save(userInChats);
            }
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.debug(e.toString());
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Произошла ошибка при удалении чата"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> showUsersInChat(String token, Long idChat) {
        try {
            User user = userRepository.findById(tokenService.getUserId(token)).orElseThrow(
                    () -> new EntityNotFoundException("Пользователь с таким id  не найден"));
            Chat chat = chatRepository.findById(idChat).orElseThrow(
                    () -> new EntityNotFoundException("Чат с таким id  не найден"));
            if (chat.getIsDeleted())
                throw new EntityNotFoundException("Чат с таким id  не найден");
            List<ResponseContactUserDto> dtos = new ArrayList<>();
            List<UserInChats> userInChatsList= chat.getUsersInChats();
            for (UserInChats userInChats : userInChatsList) {
                dtos.add(
                        new ResponseContactUserDto(userInChats.getUser().getId(), userInChats.getUser().getUsername()));
            }
            ResponseContactsDto contactsDto = new ResponseContactsDto(dtos);
            return ResponseEntity.ok(contactsDto);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.debug(e.toString());
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Произошла ошибка при добавлении"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> addUsersInChat(String token, Long idChat, RequestUsersInChatDto addUsersInChatDto) {
        try {
            User user = userRepository.findById(tokenService.getUserId(token)).orElseThrow(
                    () -> new EntityNotFoundException("Пользователь с таким id  не найден"));
            Chat chat = chatRepository.findByIdAndCreator(idChat, user).orElseThrow(
                    () -> new EntityNotFoundException("Чат с таким id  не найден"));
            if (chat.getIsDeleted())
                throw new EntityNotFoundException("Чат с таким id  не найден");
            List<User> chatUsers = new ArrayList<>();
            for (UserInChats userInChats : chat.getUsersInChats()) {
                chatUsers.add(userInChats.getUser());
            }
            List<RequestContactsDto> usersDtoList = addUsersInChatDto.getUsers();
            for (RequestContactsDto contactsDto : usersDtoList) {
                User newChatUser = userRepository.findById(contactsDto.getId()).orElseThrow(
                        () -> new EntityNotFoundException("Пользователь с таким id  не найден"));
                if (chatUsers.contains(newChatUser))
                    continue;
                userInChatsRepository.save(new UserInChats(chat, newChatUser));
            }
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.debug(e.toString());
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Произошла ошибка при добавлении"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteUsersFromChat(String token, Long idChat, RequestUsersInChatDto deleteUsersInChatDto) {
        try {
            User user = userRepository.findById(tokenService.getUserId(token)).orElseThrow(
                    () -> new EntityNotFoundException("Пользователь с таким id  не найден"));
            Chat chat = chatRepository.findByIdAndCreator(idChat, user).orElseThrow(
                    () -> new EntityNotFoundException("Чат с таким id  не найден"));
            List<RequestContactsDto> usersDtoList = deleteUsersInChatDto.getUsers();
            for (UserInChats userInChats : chat.getUsersInChats()) {
                if (usersDtoList.contains(new RequestContactsDto(userInChats.getUser().getId()))) {
                    userInChats.setIsDeleted(true);
                    userInChatsRepository.save(userInChats);
                }
            }
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.debug(e.toString());
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Произошла ошибка при удалении пользователей"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
