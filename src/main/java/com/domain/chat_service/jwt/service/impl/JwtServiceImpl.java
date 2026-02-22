package com.domain.chat_service.jwt.service.impl;

import com.domain.chat_service.jwt.properties.JwtProperties;
import com.domain.chat_service.jwt.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtProperties properties;

    @Override
    public String getUsername(String token) throws ExpiredJwtException, MalformedJwtException, UnsupportedJwtException {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(properties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
