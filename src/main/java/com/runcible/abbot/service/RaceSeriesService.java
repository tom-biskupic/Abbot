package com.runcible.abbot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.RaceSeries;
import com.runcible.abbot.model.User;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

public interface RaceSeriesService
{
    /**
     * Finds all the race series objects accessible by the specified user
     * @param page The page to start on etc
     * @param user The user that is looking for a race series.
     * @return The race series page slice
     */
    public Page<RaceSeries> findAll(Pageable page,User user);
    
    /**
     * Returns the race series with the ID specified
     * @param id The id of the race series to return
     * @return The race series with the ID specified
     * @throws UserNotPermitted The logged on user is not permitted to access this series 
     * @throws NoSuchUser The logged on user does not exist.
     */
    public RaceSeries findByID(Integer id) throws NoSuchUser, UserNotPermitted;
    
    /**
     * Updates an existing race series
     * @param series The race series to update
     * @throws UserNotPermitted Logged on user is not permitted to manage this race series
     * @throws NoSuchUser 
     */
    public void update(RaceSeries series) throws NoSuchUser, UserNotPermitted; 
    
    /**
     * Add a race series
     * @param series the series to add
     * @throws NoSuchUser 
     */
    public void add(RaceSeries series) throws NoSuchUser;
}
