package com.example.security;

import com.example.LoginRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.inject.Singleton;

@Singleton
public class TokenHandler {
    public String generateAccessToken(LoginRequest request) {
        return Jwts.builder()
                .setSubject(request.getUserName())
                .claim("userName",request.getUserName())
                .signWith(SignatureAlgorithm.HS256, Constants.JWT_SIGNING_KEY)
                .compact();
    }
}
