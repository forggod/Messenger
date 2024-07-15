package com.messenger.authorization.microservice.api.services;

import com.messenger.authorization.microservice.api.dtos.*;
import com.messenger.authorization.microservice.api.exceptions.AppError;
import com.messenger.authorization.microservice.api.store.entites.User;
import com.messenger.authorization.microservice.api.utils.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final KafkaProducerService kafkaProducerService;
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    @Value("${token.expirationAt.access}")
    private Duration lifeTime;

    public ResponseEntity<?> createAuthToken(@RequestBody LoginUserRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(), "Неправильный логин или пароль"),
                    HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        String refreshToken = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token, refreshToken,
                userService.getUserIdByUsername(userDetails.getUsername()), userDetails.getUsername()));
    }

    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        if (userService.findByUsername(registrationUserDto.getUsername()).isPresent()) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с таким именем уже существует"),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            User user = userService.createNewUser(registrationUserDto);
            kafkaProducerService.sendAddUserMessage(
                    new AddUserDtoResponse(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(),
                            user.getLastLogin(), user.getCreatedAt()));
            return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername(), user.getEmail()));
        } catch (Exception e) {
            log.debug(e.toString());
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Произошла ошибка при добавлении пользователя"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> logoutUser(String token) {
        String jwt = token.substring(7);
        try {
            redisTemplate.opsForValue().set(jwtTokenUtils.getUUId(jwt).toString(), jwt);
            redisTemplate.expire(jwtTokenUtils.getUUId(jwt).toString(), lifeTime);
            log.info("Black list token: " + jwt);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(), "Неверный токен"), HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok("Вы успешно вышли из аккаунта");
    }

    public ResponseEntity<?> refreshToken(RefreshTokenDto tokenDto) {
        try {
            UUID tokenId = jwtTokenUtils.getUUId(tokenDto.getRefreshToken());
            if (Boolean.TRUE.equals(redisTemplate.hasKey(tokenId.toString()).block()))
                throw new JwtException("Токен находится в черном списке!");
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(), "Время жизни токена вышло"),
                    HttpStatus.UNAUTHORIZED);
        } catch (SignatureException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(), "Подпись неправильная"),
                    HttpStatus.UNAUTHORIZED);
        } catch (JwtException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()),
                    HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(tokenDto.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        String refreshToken = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token, refreshToken,
                userService.getUserIdByUsername(userDetails.getUsername()), userDetails.getUsername()));
    }
}
