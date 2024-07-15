package com.messenger.services;

import com.messenger.dtos.ResponseContactUserDto;
import com.messenger.dtos.ResponseContactsDto;
import com.messenger.exceptions.AppError;
import com.messenger.store.entites.Contact;
import com.messenger.store.entites.User;
import com.messenger.store.repositories.ContactRepository;
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
public class ContactsService {
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final ContactRepository contactRepository;

    public ResponseEntity<?> getListOfContacts(String token) {
        try {
            User user = userRepository.findById(tokenService.getUserId(token)).orElseThrow(
                    () -> new EntityNotFoundException("Пользователь с таким id  не найден"));
            List<Contact> contactsList = contactRepository.findByUserFirstOrUserSecond(user);
            List<ResponseContactUserDto> contactUserDtoList = new ArrayList<>();
            for (Contact contact : contactsList) {
                if (contact.getIsDeleted())
                    continue;
                if (user == contact.getUserFirst()) {
                    contactUserDtoList.add(
                            new ResponseContactUserDto(contact.getUserSecond().getId(), contact.getUserSecond().getUsername()));
                } else {
                    contactUserDtoList.add(
                            new ResponseContactUserDto(contact.getUserFirst().getId(), contact.getUserFirst().getUsername()));
                }
            }
            return ResponseEntity.ok(new ResponseContactsDto(contactUserDtoList));
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.debug(e.toString());
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Произошла ошибка при формировании списка контактов"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> addContactById(String token, Long id) {
        try {
            User user = userRepository.findById(tokenService.getUserId(token)).orElseThrow(
                    () -> new EntityNotFoundException("Пользователь с таким id  не найден"));
            User contactUser = userRepository.findById(id).orElseThrow(
                    () -> new EntityNotFoundException("Контакт с таким id  не найден"));
            Contact contact = contactRepository.save(new Contact(user, contactUser));
            return ResponseEntity.ok(
                    new ResponseContactUserDto(contact.getId(), contact.getUserSecond().getUsername()));
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.debug(e.toString());
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Произошла ошибка при добавлении контакта"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getContactById(String token, Long id) {
        try {
            User contact = userRepository.findById(id).orElseThrow(
                    () -> new EntityNotFoundException("Контакт с таким id  не найден"));
            if (contact.getIsDeleted())
                throw new EntityNotFoundException("Контакт с таким id  не найден");
            return ResponseEntity.ok(new ResponseContactUserDto(contact.getId(), contact.getUsername()));
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getContactByUsername(String token, String username) {
        try {
            User contact = userRepository.findByUsername(username).orElseThrow(
                    () -> new EntityNotFoundException("Контакт с таким username  не найден"));
            if (contact.getIsDeleted())
                throw new EntityNotFoundException("Контакт с таким username  не найден");
            log.debug(contact.getId().toString());
            return ResponseEntity.ok(new ResponseContactUserDto(contact.getId(), contact.getUsername()));
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getContactByPhone(String token, String phone) {
        try {
            User contact = userRepository.findByPhone(phone).orElseThrow(
                    () -> new EntityNotFoundException("Контакт с таким phone  не найден"));
            if (contact.getIsDeleted())
                throw new EntityNotFoundException("Контакт с таким phone  не найден");
            return ResponseEntity.ok(new ResponseContactUserDto(contact.getId(), contact.getUsername()));
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getContactByEmail(String token, String email) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(
                    () -> new EntityNotFoundException("Пользователь с таким email  не найден"));
            return ResponseEntity.ok(new ResponseContactUserDto(user.getId(), user.getUsername()));
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }
}
