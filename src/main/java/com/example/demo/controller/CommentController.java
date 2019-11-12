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
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {


    @Autowired
    private CommentLogic commentLogic;

    private PostLogic postLogic;

    public CommentController(PostLogic postLogic, UserLogic userLogic) {
        this.postLogic = postLogic;
        this.userLogic = userLogic;
    }

    UserLogic userLogic;


    private Gson gson = new Gson();
    private Logger logger = Logger.getLogger(CommentController.class);


    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse<Comment>> createComment(@Valid @RequestBody CommentViewModel comment) {
        MessageResponse<Comment> messageResponse = new MessageResponse<>();
        Gson gson = new Gson();
        logger.info(gson.toJson(comment));
        Comment commentobj = new Comment();
        commentobj.setText(comment.getText());
        Post post = postLogic.findOne(comment.getPost());
        commentobj.setPost(post);
        commentobj.setStatus("ACTIVATED");
        commentLogic.create(commentobj);
        messageResponse.setStatus(HttpStatus.OK.value());
        messageResponse.setMessage("A user just added comment.");
        messageResponse.setSuccessful(true);
        messageResponse.setData(commentobj);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.PUT)
    public ResponseEntity<MessageResponse<Comment>> UpdateComment(@PathVariable Integer id, @Valid @RequestBody CommentViewModel comment) {
        MessageResponse<Comment> messageResponse = new MessageResponse<>();
        Gson gson = new Gson();
        logger.info(gson.toJson(comment));
        Comment commentList = commentLogic.findOne(id);
        commentList.setText(comment.getText());
        Post post = postLogic.findOne(comment.getPost());
        commentList.setPost(post);
        commentLogic.update(commentList);
//        List<Post> postList = postLogic.findOne(id);
        messageResponse.setMessage("isSuccessful");
        messageResponse.setData(commentList);
        messageResponse.setSuccessful(true);
        messageResponse.setStatus(HttpStatus.OK.value());
        HttpHeaders headers = new HttpHeaders();
        logger.info("<<<<<<<<<<<<<<<<Posts Accessed Successfully>>>>>>>>>>>>>>>");
        return new ResponseEntity<>(messageResponse, headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    public ResponseEntity<MessageResponse<List<Comment>>> getAllComments() {
        MessageResponse<List<Comment>> messageResponse = new MessageResponse<>();
        List<Comment> commenttList = commentLogic.findAll();
        messageResponse.setMessage("isSuccessful");
        messageResponse.setData(commenttList);
        messageResponse.setSuccessful(true);
        messageResponse.setStatus(HttpStatus.OK.value());
        HttpHeaders headers = new HttpHeaders();
        logger.info("<<<<<<<<<<<<<<<<Comments Accessed Successfully>>>>>>>>>>>>>>>");
        return new ResponseEntity<>(messageResponse, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/deactivatecomment/{id}", method = RequestMethod.DELETE)
    public void deleteComment(@PathVariable Integer id) {
        Comment comment = commentLogic.findOne(id);
        comment.setStatus("DEACTIVATED");
        commentLogic.update(comment);
    }

    @RequestMapping(value = "/commentsbypost", method = RequestMethod.GET)
    public ResponseEntity<MessageResponse<List<Comment>>> getAllCommentByPost(@RequestParam Integer id) {
        MessageResponse<List<Comment>> messageResponse = new MessageResponse<>();
        Post post = postLogic.findOne(id);
        List<Comment> comments = commentLogic.getByColunmName("post", post);
        messageResponse.setStatus(HttpStatus.OK.value());
        messageResponse.setMessage("isSuccessful.");
        messageResponse.setData(comments);
        messageResponse.setSuccessful(true);
        HttpHeaders headers = new HttpHeaders();
        logger.info("Successful");
        return new ResponseEntity<>(messageResponse, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/deactivatedcomments", method = RequestMethod.GET)
    public ResponseEntity<MessageResponse<List<Comment>>> getAllDeactivatedPost() {
        List<Comment> comments = commentLogic.getByColunmName("status", "DEACTIVATED");
        MessageResponse<List<Comment>> listMessageResponse = new MessageResponse<>();
        listMessageResponse.setData(comments);
        listMessageResponse.setSuccessful(true);
        listMessageResponse.setMessage("Pulled successfully");
        return new ResponseEntity<>(listMessageResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/activatedcomments", method = RequestMethod.GET)
    public ResponseEntity<MessageResponse<List<Comment>>> getActivatedCommentst() {
        List<Comment> comments = commentLogic.getByColunmName("status", "ACTIVATED");
        MessageResponse<List<Comment>> listMessageResponse = new MessageResponse<>();
        listMessageResponse.setData(comments);
        listMessageResponse.setSuccessful(true);
        listMessageResponse.setMessage("Pulled successfully");
        return new ResponseEntity<>(listMessageResponse, HttpStatus.OK);
    }
}
