package com.runcible.abbot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.runcible.abbot.model.User;

@Component
public class AuthenticationService implements AuthenticationProvider
{

    private static final String ADMIN_ROLE = "ROLE_ADMIN";
    private static final String USER_ROLE = "ROLE_USER";

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException
    {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        User user = userService.validateLogon(email, password);
        
        if ( user == null )
        {
            throw new AuthenticationCredentialsNotFoundException("Logon denied");
        }
        
        List<GrantedAuthority> roles = getRoles(user);
        
        return new UsernamePasswordAuthenticationToken(email, password, roles);
    }

    private List<GrantedAuthority> getRoles(User user)
    {
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
