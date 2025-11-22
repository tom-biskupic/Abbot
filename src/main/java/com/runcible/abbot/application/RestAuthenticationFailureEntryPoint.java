package com.runcible.abbot.application;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class RestAuthenticationFailureEntryPoint extends SimpleUrlAuthenticationFailureHandler
{
    Logger logger = Logger.getLogger(RestAuthenticationFailureEntryPoint.class.getName());

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest      request,
            HttpServletResponse     response, 
            AuthenticationException authException)            throws IOException, ServletException
    {
        logger.info("Authentication failed: "+authException.getMessage());
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        PrintWriter writer = response.getWriter();
        writer.write("Authentication failed");
        writer.flush();
        writer.close();
    }

}
