package com.messenger.services;

import com.messenger.utils.JwtTokenUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class TokenService {
    private final JwtTokenUtils jwtTokenUtils;

    public Long getUserId(String authHeader) {
        return jwtTokenUtils.getId(authHeader.substring(7));
    }
}
