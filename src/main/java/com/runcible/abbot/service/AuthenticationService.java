package com.runcible.abbot.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.runcible.abbot.model.User;

import javassist.bytecode.stackmap.TypeData.ClassName;

@Component
public class AuthenticationService implements AuthenticationProvider
{

    private static final String ADMIN_ROLE = "ROLE_ADMIN";
    private static final String USER_ROLE = "ROLE_USER";

    final static Logger logger = Logger.getLogger(AuthenticationService.class);
    
    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException
    {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        logger.info("Attempting login for "+email);
        User user = userService.validateLogon(email, password);
        
        if ( user == null )
        {
            logger.info("Login failed for "+email);
            throw new BadCredentialsException("Authentication failed for " + email);
        }
        
        List<GrantedAuthority> roles = getRoles(user);
        
        logger.info("Login succeeded for "+email);
        return new UsernamePasswordAuthenticationToken(email, password, roles);
    }

    private List<GrantedAuthority> getRoles(User user)
    {
        logger.info("Getting roles for "+user.getEmail());
        
        List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
        
        if ( user.isAdministrator() )
        {
            roles.add( new SimpleGrantedAuthority(ADMIN_ROLE));
        }
        
        roles.add(new SimpleGrantedAuthority(USER_ROLE));
        
        return roles;
    }

    @Override
    public boolean supports(Class<?> authentication)
    {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);    
    }

    @Autowired
    UserService userService;
}
