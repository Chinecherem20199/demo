package com.example.demo.controller;

import com.example.demo.business_logic.PostLogic;
import com.example.demo.business_logic.UserLogic;
import com.example.demo.model.Post;
import com.example.demo.viewmodel.PostViewModel;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.apache.log4j.Logger;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    PostLogic postLogic;
    public PostController(UserLogic userLogic) {
        this.userLogic = userLogic;
    }

    UserLogic userLogic;

    private Gson gson = new Gson();
    private Logger logger = Logger.getLogger(RoleController.class);

    @RequestMapping(value = "/savepost", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse<Post>> createPost(@Valid @RequestBody PostViewModel post){
        MessageResponse<Post> messageResponse = new MessageResponse<>();
        Gson gson=new Gson();
        logger.info(gson.toJson(post));
        Post postobj = new Post();
        postobj.setTitle(post.getTitle());
        postobj.setBody(post.getBody());

        postLogic.create(postobj);
//        Check if it will work
        messageResponse.setStatus(HttpStatus.OK.value());
        messageResponse.setMessage("New post was added successfully.");
        messageResponse.setSuccessful(true);
        messageResponse.setData(postobj);
        return new ResponseEntity<>(messageResponse,HttpStatus.OK);
    }

    @RequestMapping(value = "/allposts", method = RequestMethod.GET)
    public ResponseEntity<MessageResponse<List<Post>>> getAllPosts(
    ) {
        MessageResponse<List<Post>> messageResponse = new MessageResponse<>();

        List<Post> postList = postLogic.findAll();
        messageResponse.setMessage("isSuccessful");
        messageResponse.setData(postList);
        messageResponse.setSuccessful(true);
        messageResponse.setStatus(HttpStatus.OK.value());
        HttpHeaders headers = new HttpHeaders();
        logger.info("<<<<<<<<<<<<<<<<Posts Accessed Successfully>>>>>>>>>>>>>>>");
        return new ResponseEntity<>(messageResponse, headers, HttpStatus.OK);
    }
}