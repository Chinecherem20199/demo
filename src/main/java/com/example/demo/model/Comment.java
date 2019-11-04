package com.example.demo.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="comments")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String text;

    @ManyToOne(targetEntity = Post.class)
    private Post post;

    private User role;

    public Comment() {
    }

    public Comment(String text, Post post, User role) {
        this.text = text;

        this.post = post;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getRole() {
        return role;
    }

    public void setRole(User role) {
        this.role = role;
    }
}
