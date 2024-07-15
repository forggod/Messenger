package com.messenger.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.dtos.AddUserDto;
import com.messenger.services.UserService;
import com.messenger.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class KafkaConsumerService {
    private final ObjectMapper objectMapper;
    private final UserService userService;


    @KafkaListener(id = "MessageAddUser", topics = {"server.message.user.add"}, containerFactory = "singleFactory")
    public void consumeUserMessage(AddUserDto userDto) {
        log.info("=> consumed {}", writeValueAsString(userDto));
        userService.addNewUser(userDto);
    }

    private String writeValueAsString(AddUserDto userDto) {
        try {
            return objectMapper.writeValueAsString(userDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Writing value to JSON failed: " + userDto.toString());
        }
    }

    @KafkaListener(topics = "kafka-message-users", groupId = "consumer-message")
    public void listener(AddUserDto message) {
        log.debug(message.toString());
    }
}
