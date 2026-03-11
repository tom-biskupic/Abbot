package com.runcible.abbot.application;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Authentication entry point for the Angular SPA. API calls (paths containing
 * ".json") receive a 401 so the Angular auth interceptor can handle session
 * expiry. All other unauthenticated requests (page refreshes, deep links) are
 * redirected to the SPA root so Angular loads and handles the login flow.
 */
@Component
public class SpaAuthenticationEntryPoint implements AuthenticationEntryPoint
{
    @Override
    public void commence(
            HttpServletRequest      request,
            HttpServletResponse     response,
            AuthenticationException authException) throws IOException
    {
        if (request.getRequestURI().contains(".json"))
        {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
        else
        {
            response.sendRedirect("/");
        }
    }
}
