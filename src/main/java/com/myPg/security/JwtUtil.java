package com.myPg.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // ✅ Load from application.yml / env (INDUSTRY STANDARD)
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // ================= GENERATE TOKEN =================
    public String generateToken(String userId, String role) {

        return Jwts.builder()
                .setSubject(userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ================= VALIDATE TOKEN =================
    public void validateToken(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException ex) {
            throw new RuntimeException("JWT expired");
        } catch (UnsupportedJwtException ex) {
            throw new RuntimeException("JWT unsupported");
        } catch (MalformedJwtException ex) {
            throw new RuntimeException("JWT malformed");
        } catch (SecurityException ex) {
            throw new RuntimeException("JWT signature invalid");
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("JWT token empty");
        }
    }

    // ================= EXTRACT USER ID =================
    public String extractUserId(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ================= EXTRACT ROLE =================
    public String extractUserRole(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
