package com.runcible.abbot.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.runcible.abbot.service.AuthenticationService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter 
{
    @Autowired RestAuthenticationFailureEntryPoint restAuthFailureHandler;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception 
    {
        http.authorizeRequests()
                .antMatchers(
                        "/",
                        "/index.html", 
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/fonts/**",
                        "/views/loginform.html",
                        "/views/welcome.html",
                        "/views/registerform.html",
                        "/views/registersuccessful.html",
                        "/views/contact.html",
                        "/register",
                        "/user",
                        "/webjars/**",
                        "/logout",
                        "/login").permitAll()
           .anyRequest().authenticated().and()
           .formLogin()
               .loginProcessingUrl("/login")
               .failureHandler(restAuthFailureHandler)
               .permitAll()
               .and()
           .logout().logoutUrl("/logout").invalidateHttpSession(true).permitAll().and()                   
           .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
    
    @Override
    protected void configure( AuthenticationManagerBuilder auth) throws Exception 
    {
        auth.authenticationProvider(authService);
    }

    @Autowired 
    private AuthenticationService authService;
}
