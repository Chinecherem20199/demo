package com.example.demo.jwt;


import com.example.demo.model.User;
import com.google.gson.Gson;
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
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {

    static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);
    static final String secret ="SomeSecretForJWTGeneration";
    public static final long EXPIRATION_TIME = 5 * 60 * 60;

    public String createToken(User user, Date expireAt) {
        Map<String, Object> claims = new HashMap<>();
       if(StringUtils.hasText(user.getUsername()) && StringUtils.hasText(secret) && expireAt != null && expireAt.after(new Date()) ) {
           String secret2 = new String(Base64.encodeBase64(secret.getBytes()));
           String userMap= new Gson().toJson(user);
           claims.put("user",userMap);
           String compactJws = Jwts.builder()
                   .setClaims(claims)
                    .setSubject(user.getUsername())
                   .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, secret2)
                    .setExpiration(expireAt)
                    .compact();
            return compactJws;
        }
       return null;
    }

    /** Started from here
    * **/


    public Claims getAllClaimsFromToken(String token) {
        String secret2 = new String(Base64.encodeBase64(secret.getBytes()));
        return Jwts.parser().setSigningKey(secret2).parseClaimsJws(token).getBody();

    }
    /** End of the code
     * **/

    public boolean isValid(String token) {
        if(StringUtils.hasText(token) && StringUtils.hasText(secret)) {
            try {
                String secret2 = new String(Base64.encodeBase64(secret.getBytes()));
               Claims claims= Jwts.parser().setSigningKey(secret2).parseClaimsJws(token).getBody();
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
    // Modified code
//    public String getUserId(String token) {
//        if(StringUtils.hasText(token) && StringUtils.hasText(secret)) {
//            try {
//                String secret2 = new String(Base64.encodeBase64(secret.getBytes()));
//                return Jwts.parser().setSigningKey(secret2).parseClaimsJws(token).getBody().getSubject();
//            }  catch (JwtException e) {
//                LOGGER.error(e.getMessage());
//            }
//        }
//        return null;
//
//    }
}
