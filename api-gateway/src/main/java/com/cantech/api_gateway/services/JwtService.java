package com.cantech.api_gateway.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Component
public class JwtService {

    @Value("${jwtSecretKey}")
    private  String secretKey;

    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }


    public String getAccessToken(Integer userId){
        return Jwts
                .builder()
                .subject(userId.toString())
                .claim("role", "ADMIN")
                .claim("role", "HOTEL_MANAGER")
                .signWith(getKey())
                .compact();
    }


    public Long userIdFromToken(String token){
        return Long.valueOf(
           Jwts
                   .parser()
                   .verifyWith(getKey())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload()
                   .getSubject()
        );
    }


}
