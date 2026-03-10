package com.example.clientAPI.utils;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    private final SecretKey secretKey= Jwts.SIG.HS256.key().build();

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .claims()
                .add("email", email)
                .add("signedBy", "client-api")
                .and()
                .signWith(secretKey)
                .compact();
    }

    public String validateToken(String jwt) {
        var claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
        return claims.getSubject();
    }
}
