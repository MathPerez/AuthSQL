package com.AuthSQL.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtTokenProvider {

    private final Key secretKey;

    public JwtTokenProvider() {
        
        String secret = "";
        
        byte[] decodedKey = Decoders.BASE64.decode(secret);
        this.secretKey = new SecretKeySpec(decodedKey, "HmacSHA256");

    }

    public String createToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 86400000); 

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
