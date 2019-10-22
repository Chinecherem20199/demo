package com.example.demo.controller;

import com.example.demo.jwt.JwtService;
import com.example.demo.model.User;
import com.example.demo.repository.UserLogic;
import com.example.demo.service.UserService;
import com.example.demo.support.StringSupport;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.*;


@RestController
@RequestMapping("/api")

public class UserController {
    @Autowired
    UserLogic userLogic;

    @Autowired
    StringSupport stringSupport;
    @Autowired
    EntityManager entityManager;

    @Autowired
    UserService userService;

    private static final String USERNAME = "username";
    private static final String AUTHORIZATIONS = "permissions";
    private Gson gson = new Gson();
    private Logger logger = Logger.getLogger(UserController.class);

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public HttpEntity<Map> user() {

        Map<String, Object> result = new HashMap<String, Object>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Boolean> authorizations = new HashMap();
        for (GrantedAuthority grantedAuthority : auth.getAuthorities()) {
            authorizations.put(grantedAuthority.getAuthority(), Boolean.TRUE);
        }
        result.put(AUTHORIZATIONS, authorizations);
        String username = (String) auth.getPrincipal();
        result.put(USERNAME, username);
        logger.info("<<<<<<<<<<<<<<<<User>>>>>>>>>>>>>>>" + gson.toJson(result));
        if ("anonymousUser".equals(username)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        return new HttpEntity(result);
    }



    @RequestMapping(value = "/alluser", method = RequestMethod.GET)
    public ResponseEntity<MessageResponse<List<User>>> getAllUsers(
    ) {
        MessageResponse<List<User>> messageResponse = new MessageResponse<>();

        List<User> userList = userLogic.findAll();
        messageResponse.setMessage("isSuccessful");
        messageResponse.setData(userList);
        messageResponse.setSuccessful(true);
        messageResponse.setStatus(HttpStatus.OK.value());
        HttpHeaders headers = new HttpHeaders();
        logger.info("<<<<<<<<<<<<<<<<User Accessed Successfully>>>>>>>>>>>>>>>");
        return new ResponseEntity<>(messageResponse, headers, HttpStatus.OK);
    }
    @RequestMapping(value = "/saveuser", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse<User>> createUser( @Valid @RequestBody User user){
        MessageResponse<User> messageResponse = new MessageResponse<>();
        Gson gson=new Gson();
        logger.info(gson.toJson(user));
        userLogic.create(user);
        messageResponse.setMessage("User created successfully");
        messageResponse.setStatus(200);
        messageResponse.setSuccessful(true);
        messageResponse.setData(user);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

//    @RequestMapping(value = "/edituser", method = RequestMethod.PUT)
//    public ResponseEntity<MessageResponse<User>> editUser(@Valid @RequestBody User user)
//    {
//        MessageResponse<User> messageResponse1 = new MessageResponse<>();
//        Gson gson = new Gson();
//        logger.info(gson.toJson(user));
//        userLogic.update(user);
//        messageResponse1.setMessage("Updated successfully.");
//        messageResponse1.setStatus(200);
//        messageResponse1.setData(user);
//        return new ResponseEntity<>(messageResponse1,HttpStatus.OK);
//    }
}
