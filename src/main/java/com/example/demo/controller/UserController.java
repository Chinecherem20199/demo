package com.example.demo.controller;

import com.example.demo.business_logic.PersonLogic;
import com.example.demo.business_logic.RoleLogic;
import com.example.demo.business_logic.UserLogic;
import com.example.demo.model.Person;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.support.StringSupport;
import com.example.demo.viewmodel.UserViewModel;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")

public class UserController {
    @Autowired
    UserLogic userLogic;


    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    StringSupport stringSupport;
    @Autowired
    EntityManager entityManager;

    @Autowired
    UserService userService;

    private RoleLogic roleLogic;
    private PersonLogic personLogic;

    public UserController(RoleLogic roleLogic, PersonLogic personLogic, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleLogic = roleLogic;
        this.personLogic = personLogic;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    private static final String USERNAME = "username";
    private static final String AUTHORIZATIONS = "permissions";
    private Gson gson = new Gson();
    private Logger logger = Logger.getLogger(UserController.class);

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")

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


    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
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

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @Transactional(rollbackFor =Exception.class)
    public ResponseEntity<MessageResponse<User>> createUser(@Valid @RequestBody UserViewModel user) {
        MessageResponse<User> messageResponse = new MessageResponse<>();

        Gson gson = new Gson();
        logger.info(gson.toJson(user));
        User userObj = new User();
        userObj.setUsername(user.getUsername().trim());
        userObj.setPassword(bCryptPasswordEncoder.encode(user.getPassword().trim()));
        Person person = new Person();
        person.setPhoneNumber(user.getPhoneNumber());
        person.setEmail(user.getEmail());
        person.setFullName(user.getFullName().trim());
        person.setAddress(user.getAddress().trim());

       List<User> userList = userLogic.getByUsernameAndEmail(user.getUsername(),user.getEmail());

       if(!userList.isEmpty()){
           messageResponse.setMessage("User already exist");
           messageResponse.setStatus(403);
           messageResponse.setSuccessful(false);
           return new ResponseEntity<>(messageResponse, HttpStatus.OK);
       }
        personLogic.create(person);
        userObj.setStatus("ACTIVATED");
        Role role = roleLogic.findOne(user.getRole());
        userObj.setRole(role);
        userObj.setPerson(person);
        userLogic.create(userObj);
//        System.out.println(user);
        messageResponse.setMessage("User created successfully");
        messageResponse.setStatus(200);
        messageResponse.setSuccessful(true);
        messageResponse.setData(userObj);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public ResponseEntity<MessageResponse<Person>> updateUser(@PathVariable Integer id, @Valid @RequestBody UserViewModel user) {
        MessageResponse<Person> messageResponse = new MessageResponse<>();
        Gson gson = new Gson();
        logger.info(gson.toJson(user));
        User user1 = userLogic.findOne(id);
        Person updatePerson = user1.getPerson();
        updatePerson.setAddress(user.getAddress());
        updatePerson.setFullName(user.getFullName());
        updatePerson.setEmail(user.getEmail());
        updatePerson.setPhoneNumber(user.getPhoneNumber());
//        updatePerson.setUsername(user.getUsername());
        personLogic.update(updatePerson);
//        userLogic.updateById(id);
        messageResponse.setMessage("User was updated successfully.");
        messageResponse.setStatus(200);
        messageResponse.setSuccessful(true);
        messageResponse.setData(updatePerson);

        return new ResponseEntity<>(messageResponse, HttpStatus.OK);


    }

    @RequestMapping(value = "deactivate/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void deleteUser(@PathVariable Integer id) {
        User user = userLogic.findOne(id);
        user.setStatus("DEACTIVATED");
        userLogic.update(user);
    }

}
