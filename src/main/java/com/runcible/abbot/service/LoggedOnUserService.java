package com.runcible.abbot.service;

import com.runcible.abbot.model.User;
import com.runcible.abbot.service.exceptions.NoSuchUser;

public interface LoggedOnUserService
{
    public User getLoggedOnUser() throws NoSuchUser;
}
