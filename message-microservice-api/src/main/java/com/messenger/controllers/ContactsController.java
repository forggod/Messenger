package com.messenger.controllers;

import com.messenger.dtos.RequestFindContactByEmailDto;
import com.messenger.dtos.RequestFindContactByPhoneDto;
import com.messenger.exceptions.AppError;
import com.messenger.services.ContactsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messenger/contacts")
@RequiredArgsConstructor
public class ContactsController {
    private final ContactsService contactsService;

    //TODO: отправка списка частями
    @CrossOrigin(origins = "*")
    @GetMapping("")
    public ResponseEntity<?> showListContacts(@RequestHeader("Authorization") String token) {
        return contactsService.getListOfContacts(token);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/add-contact/{id}")
    public ResponseEntity<?> addContactById(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Неверная строка запроса"), HttpStatus.BAD_REQUEST);
        }
        return contactsService.addContactById(token, id);
    }

//    @CrossOrigin(origins = "*")
//    @GetMapping("/findBy/{id}")
//    public ResponseEntity<?> showUserById(
//            @RequestHeader("Authorization") String token,
//            @PathVariable("id") Long id) {
//        if (id == null) {
//            return new ResponseEntity<>(
//                    new AppError(HttpStatus.BAD_REQUEST.value(), "Неверная строка запроса"), HttpStatus.BAD_REQUEST);
//        }
//        return contactsService.getContactById(token, id);
//    }

    @CrossOrigin(origins = "*")
    @GetMapping("/findBy/{username}")
    public ResponseEntity<?> showUserByUsername(
            @RequestHeader("Authorization") String token,
            @PathVariable("username") String username) {
        if (username == null) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Неверная строка запроса"), HttpStatus.BAD_REQUEST);
        }
        return contactsService.getContactByUsername(token, username);
    }

//    @CrossOrigin(origins = "*")
//    @PostMapping("/findBy/phone")
//    public ResponseEntity<?> showUserByPhone(
//            @RequestHeader("Authorization") String token,
//            @RequestBody RequestFindContactByPhoneDto requestFindContactByPhoneDto) {
//        return contactsService.getContactByPhone(token, requestFindContactByPhoneDto.getPhone());
//    }
//
//    @CrossOrigin(origins = "*")
//    @GetMapping("/findBy/email")
//    public ResponseEntity<?> showUserByEmail(
//            @RequestHeader("Authorization") String token,
//            @RequestBody RequestFindContactByEmailDto requestFindContactByEmailDto) {
//        return contactsService.getContactByEmail(token, requestFindContactByEmailDto.getEmail());
//    }
}