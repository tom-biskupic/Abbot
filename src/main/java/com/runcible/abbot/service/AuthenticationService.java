package com.runcible.abbot.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.runcible.abbot.model.User;
import com.runcible.abbot.service.exceptions.NoSuchUser;

@Component
public class AuthenticationService implements UserDetailsService
{
    private static final String ADMIN_ROLE = "ROLE_ADMIN";
    private static final String USER_ROLE = "ROLE_USER";

    final static Logger logger = LogManager.getLogger(AuthenticationService.class);
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
    {
        logger.info("Attempting login for "+username);
        try
        {
            User user = userService.findByEmail(username);
            List<GrantedAuthority> roles = this.getRoles(user);
  
                    logger.info("Login succeeded for "+username);
            return new org.springframework.security.core.userdetails.User(
                username, user.getPassword(), roles);
        }
        catch( NoSuchUser e )
        {
            logger.info("Login failed for "+username);
            throw new UsernameNotFoundException("No such user: " + username);
        }
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

    @Autowired
    UserService userService;
}
