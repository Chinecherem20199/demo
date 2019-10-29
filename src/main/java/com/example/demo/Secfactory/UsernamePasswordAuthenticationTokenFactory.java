package com.example.demo.Secfactory;


import com.example.demo.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class UsernamePasswordAuthenticationTokenFactory {
    private User user;

    public UsernamePasswordAuthenticationToken create(User u) {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(u.getRole().getName());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword(), Arrays.asList(simpleGrantedAuthority));
        return authentication;
    }

    public String getPassword() {
        return this.user.getPassword();
    }


    public String getUsername() {
        return this.user.getUsername();
    }
}
