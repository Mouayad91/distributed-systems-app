package de.htwsaar.vs.gruppe05.server.helper;

import de.htwsaar.vs.gruppe05.server.enums.AuthExceptionType;
import de.htwsaar.vs.gruppe05.server.enums.StatusEnums;
import de.htwsaar.vs.gruppe05.server.exceptions.AuthTokenException;
import de.htwsaar.vs.gruppe05.server.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Helper - Capsules all JWT, Operations needed
 */
@Service
public class JWTHelper {

    @Value("${jwtsecret}")
    private String jwtSecret;

    @Value("${jwtexpiration}")
    private int jwtExpiration;

    private Key key;

    /**
     * Get token from authorization header
     *
     * @param authHeader String containing http Header
     * @return token
     */
    public static String extractToken(String authHeader) {
        return authHeader.split(" ")[1];
    }

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }


    /**
     * Get the stored value in jwt
     * @param token
     * @return Claims of token
     */
    public Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(token).getBody();
        } catch (JwtException e){
            throw new AuthTokenException("Malformed Token", AuthExceptionType.INVALID);
        }
    }

    /**
     * Checks if Token is expired
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        return this.getAllClaimsFromToken(token).getExpiration().before(new Date());
    }

    /**
     * Checks if Token is valid
     * @param token
     * @return
     */
    public boolean isInvalid(String token, User user) {
        try {
            return this.isTokenExpired(token);
        } catch (JwtException e){
            throw new AuthTokenException("TokenExpired", AuthExceptionType.EXPIRED);
        }
    }

    /**
     * Generating a new JWT token and storing userId, needed during login and signing
     * @param userId
     * @param username
     * @param role
     * @return
     */
    public String generateToken(long userId, String username, StatusEnums.Role role) {
        // Saves Hashmap in the token with userId stored in it
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", Long.toString(userId));
        claims.put("role",role.name());

        return Jwts.builder()
                .setSubject(username)
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                // token has expiration of 24 hours, 1440 = 24 hours
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(jwtExpiration).toInstant()))
                .signWith(this.key)
                .compact();
    }
}
