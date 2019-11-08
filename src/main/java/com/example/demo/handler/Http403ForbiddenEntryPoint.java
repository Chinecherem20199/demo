package com.example.demo.handler;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Http403ForbiddenEntryPoint implements AccessDeniedHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(Http403ForbiddenEntryPoint.class);

    @Autowired
    HeaderHandler headerHandler;
    Gson gson = new Gson();
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        Authentication auth
                = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            LOGGER.warn("User: " + auth.getName()
                    + " attempted to access the protected URL: "
                    + request.getRequestURI());

            headerHandler.process(request, response);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
        }

        response.sendRedirect(request.getContextPath() + "/accessDenied");
    }

}
