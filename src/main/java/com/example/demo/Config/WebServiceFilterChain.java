package com.example.demo.Config;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class WebServiceFilterChain implements Filter {


    private final Logger logger = Logger.getLogger(WebServiceFilterChain.class);


    public WebServiceFilterChain() {
        super();
        logger.info("=======================WEBSERVICEFILTERCHAIN INITIALIZED SUCCESSFULLY===============");

    }

    @Override
    public void destroy() {


    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig arg0) {


    }

}
