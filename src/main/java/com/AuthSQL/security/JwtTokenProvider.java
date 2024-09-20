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
        // Uma chave segura em Base64 com pelo menos 256 bits (32 caracteres)
        String secret = "YV9sb25nX3NlY3VyZV9iYXNlNjRfZW5jb2RlZF9zZWNyZXRfa2V5X2hlcmVub3dhbGxhbG9vYW4=";
        
        byte[] decodedKey = Decoders.BASE64.decode(secret);
        this.secretKey = new SecretKeySpec(decodedKey, "HmacSHA256");

    }

    // Gera o token com o username
    public String createToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 86400000);  // 1 dia de validade

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();
    }

    // Valida o token e retorna o username
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
