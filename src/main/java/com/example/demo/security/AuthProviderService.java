package com.example.demo.security;

import com.auth0.jwt.JWT;
import com.example.demo.Secfactory.UsernamePasswordAuthenticationTokenFactory;
import com.example.demo.handler.HeaderHandler;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;


@Component
public class AuthProviderService implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthProviderService.class);

    @Autowired
    UserService userService;
    @Autowired
    HeaderHandler headerHandler;
    @Autowired
    UsernamePasswordAuthenticationTokenFactory usernamePasswordAuthenticationTokenFactory;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

            String login = authentication.getName();
            String password = authentication.getCredentials().toString();
            LOGGER.info("Doing login username" + login);
            LOGGER.info("Doing login password " + password);
            User u = userService.isLoginValid(login, password);
            if(u != null) {
                LOGGER.info("Login successful. User: " + login);
                return usernamePasswordAuthenticationTokenFactory.create(u);
            }
        LOGGER.info("User does not exist" + login);
            throw new UsernameNotFoundException("Not valid login/password");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

}
