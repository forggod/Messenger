package com.messenger.authorization.microservice.api.controllers;

import com.messenger.authorization.microservice.api.dtos.LoginUserRequest;
import com.messenger.authorization.microservice.api.dtos.RefreshTokenDto;
import com.messenger.authorization.microservice.api.dtos.RegistrationUserDto;
import com.messenger.authorization.microservice.api.services.KafkaProducerService;
import com.messenger.authorization.microservice.api.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    private final AuthService authService;
    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/api/auth/login")
    public ResponseEntity<?> createAuthToken(@RequestBody LoginUserRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/api/auth/reg")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        return authService.createNewUser(registrationUserDto);
    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String token) {
        return authService.logoutUser(token);
    }

    @PostMapping("/api/auth/token/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDto tokenDto) {
        return authService.refreshToken(tokenDto);
    }
}
