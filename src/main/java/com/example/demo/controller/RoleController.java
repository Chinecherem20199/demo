package com.example.demo.controller;

import com.example.demo.business_logic.RoleLogic;
import com.example.demo.business_logic.UserLogic;
import com.example.demo.model.Role;
import com.example.demo.viewmodel.RoleViewModel;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class RoleController {
    @Autowired
    RoleLogic roleLogic;

    public RoleController(UserLogic userLogic) {
        this.userLogic = userLogic;
    }

    UserLogic userLogic;

    private Gson gson = new Gson();
    private Logger logger = Logger.getLogger(RoleController.class);

    @RequestMapping(value = "/saverole", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse<Role>> createRole(@Valid @RequestBody RoleViewModel role){
        MessageResponse<Role> messageResponse = new MessageResponse<>();
        Gson gson=new Gson();
        logger.info(gson.toJson(role));
        Role roleobj = new Role();
        roleobj.setName(role.getName());
        roleLogic.create(roleobj);
//        Check if it will work
        messageResponse.setStatus(HttpStatus.OK.value());
        messageResponse.setMessage("New role was created successfully.");
        messageResponse.setSuccessful(true);
        messageResponse.setData(roleobj);
        return new ResponseEntity<>(messageResponse,HttpStatus.OK);
    }
//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public ResponseEntity<MessageResponse<User>> loginUser(@Valid  UserViewModel userLogin){
//        MessageResponse<User> messageResponse = new MessageResponse<>();
//        Gson gson = new Gson();
//        logger.info(gson.toJson(userLogin));
//    }
}
