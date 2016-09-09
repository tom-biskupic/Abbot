package com.runcible.abbot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.Race;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

public interface RaceService
{
    /**
     * Returns all the races in a race series based on the pageable
     * @param seriesId The ID of the race series we are searching
     * @param p A pageageble describing what to return
     * @return A page of results
     * @throws UserNotPermitted 
     * @throws NoSuchUser 
     */
    public Page<Race> getAllRacesForSeries(Integer seriesId, Pageable p) throws NoSuchUser, UserNotPermitted;
    
    /**
     * Add a race to the database.
     * @param race The race to add
     * @throws UserNotPermitted 
     * @throws NoSuchUser 
     */
    public void addRace(Integer raceSeriesId, Race race) throws NoSuchUser, UserNotPermitted;

    /**
     * Updates a race
     * @param race The race to update
     * @throws UserNotPermitted 
     * @throws NoSuchUser 
     */
    public void updateRace(Race race) throws NoSuchUser, UserNotPermitted;

    /**
     * Returns the race matching the ID provided
     * @param raceID The ID of the race to return
     * @return The race
     * @throws UserNotPermitted 
     * @throws NoSuchUser 
     */
    public Race getRaceByID(Integer raceID) throws NoSuchUser, UserNotPermitted;
    
    /**
     * Removes the race matching the ID provided
     * @param raceID The ID of the race to remove
     * @throws UserNotPermitted 
     * @throws NoSuchUser 
     */
    public void removeRace(Integer raceID) throws NoSuchUser, UserNotPermitted;
}
