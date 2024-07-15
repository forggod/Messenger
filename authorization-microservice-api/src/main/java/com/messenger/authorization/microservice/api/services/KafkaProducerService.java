package com.messenger.authorization.microservice.api.services;

import com.messenger.authorization.microservice.api.dtos.AddUserDtoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<Long, AddUserDtoResponse> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<Long, AddUserDtoResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAddUserMessage(AddUserDtoResponse userDto) {
        kafkaTemplate.send("server.message.user.add", userDto);
    }
}
