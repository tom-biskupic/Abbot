package com.runcible.abbot.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

public class AuthorizedService
{

    public AuthorizedService()
    {
        super();
    }

    protected void throwIfUserNotPermitted(Integer seriesId) throws NoSuchUser,
            UserNotPermitted
    {
        if ( ! raceSeriesAuthorizationService.isLoggedOnUserPermitted(seriesId) )
        {
            throw new UserNotPermitted();
        }
    }

    @Autowired 
    private RaceSeriesAuthorizationService raceSeriesAuthorizationService;
}