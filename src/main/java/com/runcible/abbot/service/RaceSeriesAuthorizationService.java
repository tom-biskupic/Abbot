package com.runcible.abbot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.RaceSeries;
import com.runcible.abbot.model.User;
import com.runcible.abbot.model.UserSummary;
import com.runcible.abbot.service.exceptions.CannotDeAuthorizeLastUser;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;

public interface RaceSeriesAuthorizationService
{

    /**
     * Returns true if the logged on user is permitted to manage the race series
     * with ID provided
     * @param raceSeriesID The ID of the race series being managed.
     * @return
     * @throws NoSuchUser If the logged on user is not recognized.
     */
    public boolean isLoggedOnUserPermitted(Integer raceSeriesID) throws NoSuchUser;
    
    /**
     * Authorizes the user provided to manage the race series provided
     * @param series The series to be managed
     * @param user The user being authorized.
     * @throws UserNotPermitted If the logged on user is not permitted to manage this race series
     * @throws NoSuchUser The logged on user is not recognized
     */
    public void authorizeUserForRaceSeries(RaceSeries series, User user) throws UserNotPermitted, NoSuchUser;
    
    /**
     * Authorizes the user provided to manage the race series provided
     * @param raceSeriesId The ID of the series to be managed
     * @param emailAddress The email address of the user being authorized.
     * @throws UserNotPermitted If the logged on user is not permitted to manage this race series
     * @throws NoSuchUser The logged on user is not recognized
     */
    public void authorizeUserForRaceSeries(Integer raceSeriesId, String emailAddress) 
    		throws UserNotPermitted, NoSuchUser, NoSuchRaceSeries;
    
    /**
     * De-Authorizes a user from a race series. Will fail if you try an de-authorize the last user.
     * @param raceSeriesID The ID of the race series being modified
     * @param userId The ID of the user being de-authorized
     * @throws UserNotPermitted The logged on user is not permitted to manage this race series
     * @throws NoSuchUser 
     * @throws CannotDeAuthorizeLastUser If there is only one user authorized for the race series.
     */
    public void deAuthorizeUserForRaceSeries(Integer raceSeriesID, Integer userId) 
    		throws NoSuchUser, UserNotPermitted, CannotDeAuthorizeLastUser;
    
    /**
     * Returns the list of user authorized to access the race series with ID specified chunked into pages
     * @param raceSeriesID The id of the race series we are returning the list of authorized users for.
     * @param page The details of the page being requested.
     * @return The list of authorized users
     * @throws UserNotPermitted The logged on user is not permitted to manage this race series
     * @throws NoSuchUser 
     */
    public Page<UserSummary> getAuthorizedUsers(Integer raceSeriesID, Pageable page) throws NoSuchUser, UserNotPermitted;
}
