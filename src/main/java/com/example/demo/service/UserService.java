package com.example.demo.service;


import com.example.demo.business_logic.UserLogic;
import com.example.demo.handler.AjaxAuthenticationSuccessHandler;
import com.example.demo.jwt.JwtService;
import com.example.demo.model.User;
import com.example.demo.support.DateGenerator;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;


@Component
public class UserService {


    private JwtService jwtService;
    private DateGenerator dateGenerator;

    private UserLogic userLogic;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserService(JwtService jwtService, DateGenerator dateGenerator, UserLogic userLogic, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.jwtService = jwtService;
        this.dateGenerator = dateGenerator;
        this.userLogic = userLogic;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

    }

    private static String DEACTIVATED = "DEACTIVATED";

    public User isLoginValid(String username, String pass) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(bCryptPasswordEncoder.encode(pass))) {
            return null;
        }
        List<User> userList = userLogic.getByColunmName("userName", username);
        if (userList == null || userList.isEmpty()) {
            return null;
        }
        if (userList.get(0).getStatus().equals(DEACTIVATED)) {
            return null;
        }
        User u = userList.get(0);
        return u;
    }

    private final Logger logger = Logger.getLogger(AjaxAuthenticationSuccessHandler.class);


    public User createUserToken(String username, String secret) {


        User u = userLogic.getByColunmName("userName", username).get(0);
        String token = jwtService.createToken(u, dateGenerator.getExpirationDate());
        u.setToken(token);
        logger.info("=================Token===========" + token);

//        userLogic.update(u);
        return u;
    }
    private final Logger logger1 = Logger.getLogger(JwtService.class);

    public User validateUser(String token, String userId) {
        String username = jwtService.getUsername(token);
        if (username != null && userId !=null) {
            User user = userLogic.getByColunmName("userName", username).get(0);
            if (user != null && jwtService.isValid(token)) {
               Claims claims=jwtService.getAllClaimsFromToken(token);
                 Object userClaims = claims.get("user");
                 logger.info(new Gson().toJson(userClaims));
                Gson gson = new Gson();
                User userObject = gson.fromJson(userClaims.toString(), User.class);
                 if(userObject.getId().toString().equals(userId)){
                     logger.info(new Gson().toJson(claims));
                     return user;
                 }
            }
        }
        return null;
    }
}
