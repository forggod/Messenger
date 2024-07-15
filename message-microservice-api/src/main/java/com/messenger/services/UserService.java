package com.messenger.services;

import com.messenger.dtos.AddUserDto;
import com.messenger.dtos.UserOnlineStatusDto;
import com.messenger.dtos.UsersDto;
import com.messenger.exceptions.AppError;
import com.messenger.store.entites.User;
import com.messenger.store.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity<?> addNewUser(AddUserDto addUserDto) {
        if (userRepository.existsByUsername(addUserDto.getUsername())
                || userRepository.existsByEmail(addUserDto.getEmail())
                || userRepository.existsByPhone(addUserDto.getPhone())) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с такими данными уже существует"),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            User user = new User();
            user.setId(addUserDto.getId());
            user.setUsername(addUserDto.getUsername());
            user.setEmail(addUserDto.getEmail());
            user.setPhone(addUserDto.getPhone());
            user.setLastLogin(addUserDto.getLastLogin());
            user.setCreatedAt(addUserDto.getCreatedAt());
            user.setIsOnline(true);
            user.setIsDeleted(false);
            userRepository.save(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.debug(e.toString());
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Произошла ошибка при добавлении пользователя"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> changeOnlineStatus(UserOnlineStatusDto userOnlineStatusDto) {
        try {
            User user = userRepository.findById(userOnlineStatusDto.getId()).orElseThrow(
                    () -> new EntityNotFoundException("Пользователь с таким id  не найден"));
            user.setIsOnline(userOnlineStatusDto.getIsOnline());
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deleteUserById(long idUser) {
        try {
            User user = userRepository.findById(idUser).orElseThrow(
                    () -> new UsernameNotFoundException("Невозможно найти и удалить пользователя с таким id "));
            user.setIsDeleted(true);
        } catch (UsernameNotFoundException ex) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> showusers() {
        List<User> userList = (List<User>) userRepository.findAll();
        return ResponseEntity.ok(new UsersDto(userList));
    }
}
