
package com.example.demo.handler;


import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


@Component
public class AjaxAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    HeaderHandler headerHandler;
    @Autowired
    UserService userService;
    Gson gson = new Gson();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.debug("Authentication Successful"+"===User remote header address is equal to====="+request.getRemoteAddr());
        User u = userService.createUserToken(authentication.getName(), request.getRemoteAddr());
        Map<Object, Object> map = new HashMap<>();
        map.put("status", HttpServletResponse.SC_OK);
        map.put("message", "logged in successfully");
        map.put("user", u);
        map.put("isSuccessful", Boolean.TRUE);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(gson.toJson(map));
        response.setStatus(HttpServletResponse.SC_OK);
        headerHandler.process(request, response);
    }

}
