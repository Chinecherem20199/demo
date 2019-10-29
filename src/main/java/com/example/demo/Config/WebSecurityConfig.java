package com.example.demo.Config;


import com.example.demo.filter.JwtAuthenticationTokenFilter;
import com.example.demo.handler.AjaxAuthenticationFailureHandler;
import com.example.demo.handler.AjaxAuthenticationSuccessHandler;
import com.example.demo.handler.AjaxLogoutSuccessHandler;
import com.example.demo.handler.Http401UnauthorizedEntryPoint;
import com.example.demo.security.AuthProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;
    @Autowired
    AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;
    @Autowired
    AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;
    @Autowired
    Http401UnauthorizedEntryPoint authenticationEntryPoint;
    @Autowired
    AuthProviderService authProvider;
    @Autowired
    SecurityProperties security;
    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/user").permitAll()
                .antMatchers("/api/createuser").permitAll()
                .antMatchers("/api/alluser").permitAll()
                .antMatchers("/api/saveuser").permitAll()
                .antMatchers("/api/saverole").permitAll()
                .antMatchers("/api/edituser").permitAll()
//                .hasAnyRole("role")

//                .hasAnyAuthority("USER")
                .antMatchers("/api/allposts","/api/savepost").hasAnyAuthority("ADMIN")
                .antMatchers("/").permitAll()
                .antMatchers("/shutdown").permitAll()
                .antMatchers("/favicon.ico").permitAll()


                .and()
                .formLogin()
                .loginProcessingUrl("/api/authentication")
                .successHandler(ajaxAuthenticationSuccessHandler)
                .failureHandler(ajaxAuthenticationFailureHandler)
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                .logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(ajaxLogoutSuccessHandler)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");

        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.headers().cacheControl();
    }

}
