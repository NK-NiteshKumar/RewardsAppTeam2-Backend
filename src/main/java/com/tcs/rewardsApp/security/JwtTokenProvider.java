package com.tcs.rewardsApp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // MUST be >= 32 characters for HS256
    private static final String JWT_SECRET =
            "RewardsAppJwtSecretKeyRewardsAppJwtSecretKey";

    private final SecretKey key =
            Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

    public String generateToken(String username, String role) {

        return Jwts.builder()
                .setSubject(username)          // ✅ 0.11.5 syntax
                .claim("role", role)
                .setIssuedAt(new Date())
                // no expiration as per requirement
                .signWith(key, SignatureAlgorithm.HS256) // ✅ IMPORTANT
                .compact();
    }

    public String getUsername(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public String getRole(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }
}
