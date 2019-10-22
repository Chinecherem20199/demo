package com.example.demo.support;


import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StringSupport {

    public String generate() {
        return BCrypt.gensalt();
    }

}
