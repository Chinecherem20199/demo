package com.example.demo.viewmodel;

import javax.validation.constraints.NotNull;

public class CommentViewModel {

    @NotNull
    String text;
    private Integer post;
    private Integer user;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getPost() {
        return post;
    }

    public void setPost(Integer post) {
        this.post = post;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }
}
