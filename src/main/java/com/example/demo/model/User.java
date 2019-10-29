package com.example.demo.model;


import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    //    @Column(nullable = false)
//    @Column(name = "userName")
    String userName;

    String password;

    String salt;
    @ManyToOne(targetEntity = Role.class)
    private Role role;
//    @Column(name = "token")
    String token;

    public User() {

    }


    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
