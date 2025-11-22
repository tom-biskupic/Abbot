package com.runcible.abbot.service;

import java.security.Principal;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.runcible.abbot.model.User;
import com.runcible.abbot.service.exceptions.NoSuchUser;

@Component
public class LoggedOnUserServiceImpl implements LoggedOnUserService
{
    Logger logger = Logger.getLogger(LoggedOnUserServiceImpl.class.getName());

    @Override
    public User getLoggedOnUser() throws NoSuchUser
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = "";

        if (principal instanceof UserDetails) 
        {
            UserDetails userDetails = (UserDetails) principal;
            userEmail = userDetails.getUsername();
        }
        logger.info("Logged on user is "+userEmail);

        return userService.findByEmail(userEmail);
    }

    @Autowired
    private UserService userService;
}
