package com.example.demo.controller;

import com.example.demo.business_logic.CommentLogic;
import com.example.demo.business_logic.PostLogic;
import com.example.demo.business_logic.UserLogic;
import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.viewmodel.CommentViewModel;
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
public class CommentController {


    @Autowired
   private CommentLogic commentLogic;

    private PostLogic postLogic;

    public CommentController(PostLogic postLogic, UserLogic userLogic){
       this.postLogic= postLogic;
       this.userLogic = userLogic;
    }
    UserLogic userLogic;



    private Gson gson = new Gson();
    private Logger logger = Logger.getLogger(CommentController.class);


    @RequestMapping(value = "/savecomment", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse<Comment>> createPost(@Valid @RequestBody CommentViewModel comment){
        MessageResponse<Comment> messageResponse = new MessageResponse<>();
        Gson gson=new Gson();
        logger.info(gson.toJson(comment));
        Comment commentobj = new Comment();
        commentobj.setText(comment.getText());
        Post  post =postLogic.findOne(comment.getPost());
        commentobj.setPost(post);
        commentLogic.create(commentobj);
//        Check if it will work
        messageResponse.setStatus(HttpStatus.OK.value());
        messageResponse.setMessage("A user just added comment.");
        messageResponse.setSuccessful(true);
        messageResponse.setData(commentobj);
        return new ResponseEntity<>(messageResponse,HttpStatus.OK);
    }
}
