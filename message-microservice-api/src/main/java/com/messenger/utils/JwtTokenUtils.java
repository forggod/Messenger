package com.messenger.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class JwtTokenUtils {
    @Value("${token.secret}")
    private String secret;

    public String getUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public List<String> getRoles(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }

    public UUID getUUId(String token) {
        return UUID.fromString(getAllClaimsFromToken(token).get("id_token", String.class));
    }

    public Long getId(String token) {
        return getAllClaimsFromToken(token).get("id_user", Long.class);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
