package com.runcible.abbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.runcible.abbot.model.User;
import com.runcible.abbot.service.exceptions.NoSuchUser;

@Component
public class LoggedOnUserServiceImpl implements LoggedOnUserService
{

    @Override
    public User getLoggedOnUser() throws NoSuchUser
    {
        String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        return userService.findByEmail(userEmail);
    }

    @Autowired
    private UserService userService;
}
