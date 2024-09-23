package com.AuthSQL.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.SignatureException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;

import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtTokenProvider {

    private final Key secretKey;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        
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


    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            
            Claims claims = getAllClaimsFromToken(token);

            
            if (claims.getExpiration().before(new Date())) {
                return false;
            }

            String username = claims.getSubject();
            return username.equals(userDetails.getUsername());
        } catch (ExpiredJwtException e) {
            
            System.out.println("Token expirado");
            return false;
        } catch (SignatureException e) {
            
            System.out.println("Assinatura inválida");
            return false;
        } catch (Exception e) {
            
            System.out.println("Token inválido");
            return false;
        }
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
