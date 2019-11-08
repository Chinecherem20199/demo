package com.example.demo.service;


import com.example.demo.business_logic.UserLogic;
import com.example.demo.handler.AjaxAuthenticationSuccessHandler;
import com.example.demo.jwt.JwtService;
import com.example.demo.model.User;
import com.example.demo.support.DateGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;


@Component
public class UserService {


    private JwtService jwtService;
    private DateGenerator dateGenerator;

    private UserLogic userLogic;

    @Autowired
    public UserService(JwtService jwtService, DateGenerator dateGenerator, UserLogic userLogic
    ) {

        this.jwtService = jwtService;
        this.dateGenerator = dateGenerator;
        this.userLogic = userLogic;

    }

    private static String DEACTIVATED = "DEACTIVATED";

    public User isLoginValid(String username, String pass) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(pass)) {
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

        String token = jwtService.createToken(username, dateGenerator.getExpirationDate());
        User u = userLogic.getByColunmName("userName", username).get(0);
        u.setToken(token);
        logger.info("=================Token===========" + token);

//        userLogic.update(u);
        return u;
    }

    public User validateUser(String token) {
        String username = jwtService.getUsername(token);
        if (username != null) {
            User user = userLogic.getByColunmName("userName", username).get(0);
            if (user != null && jwtService.isValid(token)) {
                return user;
            }
        }
        return null;
    }
}
