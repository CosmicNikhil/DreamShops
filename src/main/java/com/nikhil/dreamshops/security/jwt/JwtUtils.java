package com.nikhil.dreamshops.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.nikhil.dreamshops.security.user.ShopUserDetails;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtils {

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.token.expirationInMils}")
    private int expirationTime;

    // Generate JWT token for the authenticated user
    public String generateTokenForUser(Authentication authentication) {
        ShopUserDetails userPrincipal = (ShopUserDetails) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .claim("id", userPrincipal.getId())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // Use System.currentTimeMillis()
                .signWith(getSigningKey(), io.jsonwebtoken.SignatureAlgorithm.HS256) // Use getSigningKey() for key
                .compact();
    }

    // Retrieve the username (email) from the JWT token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Set the key
                .build()
                .parseClaimsJws(token) // Parse the token
                .getBody()
                .getSubject(); // Get the subject (email) from the token
    }

    // Validate the JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Set the key
                .build()
                .parseClaimsJws(token); // If token parsing succeeds, it's valid
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            throw new JwtException(e.getMessage());
        }
    }

    // Helper method to retrieve the signing key
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret); // Decode the secret
        return Keys.hmacShaKeyFor(keyBytes); // Generate the signing key
    }
}
