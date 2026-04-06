package com.smartspend.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}") private String secret;
    @Value("${jwt.expiration}") private long expiration;

    private Key key() { return Keys.hmacShaKeyFor(secret.getBytes()); }

    public String generate(String email, String role) {
        return Jwts.builder().setSubject(email).claim("role", role)
            .setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(key(), SignatureAlgorithm.HS256).compact();
    }

    public String email(String token) { return claims(token).getSubject(); }
    public String role(String token) { return (String) claims(token).get("role"); }
    public boolean valid(String token) {
        try { claims(token); return true; } catch (Exception e) { return false; }
    }
    private Claims claims(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
    }
}
