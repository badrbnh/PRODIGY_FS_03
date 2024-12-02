package com.prodigy.server.auth.service;


import com.prodigy.server.auth.model.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author badreddine
 **/
@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secretKey;


    public String generateToken(String username, TokenType tokenType) {
        Date expiration = tokenType == TokenType.ACCESS_TOKEN
                ? new Date(System.currentTimeMillis() + 1000 * 60 * 15)
                : new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7);



        Map<String, Object> claims = new HashMap<>();
        claims.put("typ", tokenType.name());

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiration)
                .and()
                .signWith(getKey())
                .compact();
    }

    public TokenResponse generateJWT(String username) {
        String accessToken = generateToken(username, TokenType.ACCESS_TOKEN);
        String refreshToken = generateToken(username, TokenType.REFRESH_TOKEN);
        return new TokenResponse(accessToken, refreshToken);
    }

    private SecretKey getKey() {

        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get("typ", String.class));
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
