package com.example.demo.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
public class JwtService {

    static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);
    static final String secret ="SomeSecretForJWTGeneration";
    public static final long EXPIRATION_TIME = 5 * 60 * 60;

    public String createToken(String username, Date expireAt) {
       if(StringUtils.hasText(username) && StringUtils.hasText(secret) && expireAt != null && expireAt.after(new Date()) ) {
           String secret2 = new String(Base64.encodeBase64(secret.getBytes()));
           String compactJws = Jwts.builder()
                    .setSubject(username)
                   .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, secret2)
                    .setExpiration(expireAt)
                    .compact();
            return compactJws;
        }
       return null;
    }

    public boolean isValid(String token) {
        if(StringUtils.hasText(token) && StringUtils.hasText(secret)) {
            try {
                String secret2 = new String(Base64.encodeBase64(secret.getBytes()));
                Jwts.parser().setSigningKey(secret2).parseClaimsJws(token);
                return true;
            } catch (JwtException e) {
                LOGGER.error(e.getMessage());
            }
        }
        return false;
    }

    public String getUsername(String token) {
        if(StringUtils.hasText(token) && StringUtils.hasText(secret)) {
            try {
                String secret2 = new String(Base64.encodeBase64(secret.getBytes()));
                return Jwts.parser().setSigningKey(secret2).parseClaimsJws(token).getBody().getSubject();
            }  catch (JwtException e) {
                LOGGER.error(e.getMessage());
            }
        }
        return null;
    }
    //for retrieveing any information from token we will need the secret key

    private Claims getAllClaimsFromToken(String token) {

        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

    }

}
