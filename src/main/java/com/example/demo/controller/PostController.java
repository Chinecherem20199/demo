package com.example.demo.controller;

import com.example.demo.business_logic.CommentLogic;
import com.example.demo.business_logic.PostLogic;
import com.example.demo.business_logic.UserLogic;
import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import com.example.demo.model.User;
import com.example.demo.viewmodel.PostViewModel;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    PostLogic postLogic;


    private static String DEACTIVATED = "DEACTIVATED";
    private static String ACTIVATED = "ACTIVATED";

    CommentLogic commentLogic;


    public PostController(UserLogic userLogic, CommentLogic commentLogic) {
        this.userLogic = userLogic;
        this.commentLogic = commentLogic;
    }

    UserLogic userLogic;

    private Gson gson = new Gson();
    private Logger logger = Logger.getLogger(RoleController.class);

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse<Post>> createPost(@Valid @RequestBody PostViewModel post){
        MessageResponse<Post> messageResponse = new MessageResponse<>();
        Gson gson=new Gson();
        logger.info(gson.toJson(post));
        Post postobj = new Post();
        postobj.setTitle(post.getTitle());
        postobj.setBody(post.getBody());
        User user = userLogic.findOne(post.getUser());
        postobj.setUser(user);
        postobj.setStatus("ACTIVATED");
        postLogic.create(postobj);
//        Check if it will work
        messageResponse.setStatus(HttpStatus.OK.value());
        messageResponse.setMessage("New post was added successfully.");
        messageResponse.setSuccessful(true);
        messageResponse.setData(postobj);
        return new ResponseEntity<>(messageResponse,HttpStatus.OK);
    }

    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public ResponseEntity<MessageResponse<List<Post>>> getAllPosts() {
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

    @RequestMapping(value = "post/{id}", method = RequestMethod.GET)
    public ResponseEntity<MessageResponse<Post>> GetPostsById(@RequestParam Integer id) {
        MessageResponse<Post> messageResponse = new MessageResponse<>();
        Gson gson = new Gson();
        logger.info(gson.toJson(messageResponse));
        Post post = postLogic.findOne(id);
        messageResponse.setMessage("isSuccessful");
        messageResponse.setData(post);
        messageResponse.setSuccessful(true);
        messageResponse.setStatus(HttpStatus.OK.value());
        HttpHeaders httpHeaders = new HttpHeaders();
        logger.info("Posts accessed successfully.");
        return new ResponseEntity<>(messageResponse, httpHeaders, HttpStatus.OK);
    }


    @RequestMapping(value = "/posts/{id}", method = RequestMethod.PUT)
    public ResponseEntity<MessageResponse<Post>> UpdatePosts(@PathVariable Integer id, @Valid @RequestBody PostViewModel post) {
        MessageResponse<Post> messageResponse = new MessageResponse<>();
        Gson gson = new Gson();
        logger.info(gson.toJson(post));
        Post postList = postLogic.findOne(id);
        postList.setTitle(post.getTitle());
        postList.setBody(post.getBody());
        User user = userLogic.findOne(post.getUser());
        postList.setUser(user);
        postLogic.update(postList);
//        List<Post> postList = postLogic.findOne(id);
        messageResponse.setMessage("isSuccessful");
        messageResponse.setData(postList);
        messageResponse.setSuccessful(true);
        messageResponse.setStatus(HttpStatus.OK.value());
        HttpHeaders headers = new HttpHeaders();
        logger.info("<<<<<<<<<<<<<<<<Posts Accessed Successfully>>>>>>>>>>>>>>>");
        return new ResponseEntity<>(messageResponse, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "deactivatepost/{id}", method = RequestMethod.DELETE)
    public void deletePost(@PathVariable Integer id) {
       Post post = postLogic.findOne(id);
        post.setStatus("DEACTIVATED");
        postLogic.update(post);
    }

    @RequestMapping(value = "deactivatedposts", method = RequestMethod.GET)
    public  ResponseEntity<MessageResponse<List<Post>> >getAllDeactivatedPost() {
        List<Post> post = postLogic.getByColunmName("status","DEACTIVATED");
        MessageResponse<List<Post>>  listMessageResponse=new MessageResponse<>();
        listMessageResponse.setData(post);
        listMessageResponse.setSuccessful(true);
        listMessageResponse.setMessage("Pulled successfully");
        return new ResponseEntity<>(listMessageResponse,HttpStatus.OK);
    }

    @RequestMapping(value = "activatedposts", method = RequestMethod.GET)
    public  ResponseEntity<MessageResponse<List<Post>> >getActivatedPost() {
        List<Post> post = postLogic.getByColunmName("status","ACTIVATED");
        MessageResponse<List<Post>>  listMessageResponse=new MessageResponse<>();
        listMessageResponse.setData(post);
        listMessageResponse.setSuccessful(true);
        listMessageResponse.setMessage("Pulled successfully");
        return new ResponseEntity<>(listMessageResponse,HttpStatus.OK);
    }
}
