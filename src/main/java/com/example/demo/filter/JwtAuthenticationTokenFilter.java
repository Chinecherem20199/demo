package com.example.demo.filter;


import com.example.demo.Secfactory.UsernamePasswordAuthenticationTokenFactory;
import com.example.demo.model.User;
import com.example.demo.security.SecurityAppContext;
import com.example.demo.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String UTF_8 = "UTF-8";
    //Begin index for bear. bc index of bearer is 7
    private static final int BEGIN_INDEX = 7;
    private final Logger logger = Logger.getLogger(this.getClass());


    private UserService userService;

    private UsernamePasswordAuthenticationTokenFactory usernamePasswordAuthenticationTokenFactory;

    private SecurityAppContext securityAppContext;


    @Autowired
    public JwtAuthenticationTokenFilter(UserService userService, SecurityAppContext securityAppContext,
                                        UsernamePasswordAuthenticationTokenFactory usernamePasswordAuthenticationTokenFactory) {
        this.securityAppContext = securityAppContext;
        this.userService = userService;
        this.usernamePasswordAuthenticationTokenFactory = usernamePasswordAuthenticationTokenFactory;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authToken = request.getHeader(AUTHORIZATION);
        if (authToken != null) {
            try {
                authToken = new String(authToken.substring(BEGIN_INDEX).getBytes(), UTF_8);
                SecurityContext context = securityAppContext.getContext();
                if (context.getAuthentication() == null) {
                    logger.info("Checking authentication for token " + authToken);
                    User u = userService.validateUser(authToken);
                    if (u != null) {
                        logger.info("===========User " + u.getUsername() + " found.=========");
                        Authentication authentication = usernamePasswordAuthenticationTokenFactory.create(u);
                        context.setAuthentication(authentication);
                    }
                }
            } catch (StringIndexOutOfBoundsException e) {
                logger.error(e.getMessage());
            }

        }
        chain.doFilter(request, response);
    }

}