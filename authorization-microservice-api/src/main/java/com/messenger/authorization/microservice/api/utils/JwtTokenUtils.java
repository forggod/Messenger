package com.messenger.authorization.microservice.api.utils;

import com.messenger.authorization.microservice.api.store.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenUtils {
    private final UserRepository userRepository;

    @Value("${token.secret}")
    private String secret;
    @Value("${token.expirationAt.access}")
    private Duration lifeTime;
    @Value("${token.expirationAt.refresh}")
    private Duration refreshLifeTime;

    public JwtTokenUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roleList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roleList);
        claims.put("id_user", userRepository.findByUsername(userDetails.getUsername()).get().getId());
        claims.put("id_token", UUID.randomUUID());

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + lifeTime.toMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id_user", userRepository.findByUsername(userDetails.getUsername()).get().getId());
        claims.put("id_token", UUID.randomUUID());

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + refreshLifeTime.toMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public UUID getUUId(String token) {
            return UUID.fromString(getAllClaimsFromToken(token).get("id_token", String.class));
    }

    public List<String> getRoles(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }

    // getId

    private Claims getAllClaimsFromToken(String token) {
        try {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        } catch (JwtException e) {
            log.debug(e.toString());
            return null;
        }
    }
}
