package com.rsg.webflux.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtParser;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JWTService {

    private final SecretKey key;
    private final JwtParser parser;

    public JWTService() {
        this.key = Keys.hmacShaKeyFor("A very secret key that is at least 32 bytes long!".getBytes());
        this.parser = Jwts.parser().setSigningKey(this.key).build();
    }

    /**
     * To generate token for users
     * We use JwtBuilder to create token with the given username and time limit and isgning it withour key
     */
    public String generateToken(String userName) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .subject(userName)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(10, ChronoUnit.MINUTES)))
                .signWith(key);
        return jwtBuilder.compact();
    }

    /**
     * Used to retrieve the name from token using Claims
     * return subject since we set subject as username in token
     *
     * @param token
     * @return
     */
    public String getUserName(String token) {
        Claims claims = parser.parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

    /**
     * Used to validate the token from user
     *
     * @param userName
     * @param token
     * @return
     */
    public boolean validare(String userName, String token) {
        Claims claims = parser.parseSignedClaims(token).getPayload();

        return claims.getExpiration().after(Date.from(Instant.now())) && claims.getSubject().equals(userName);
    }

}
