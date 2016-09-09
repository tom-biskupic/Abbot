package com.runcible.abbot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.Fleet;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

public interface FleetService
{
    /**
     * Adds the specified fleet to the database
     * @param fleet
     * @throws NoSuchRaceSeries The race series ID provided doesn't match a race series
     * @throws UserNotPermitted The logged on user is not permitted to manage this race series
     * @throws NoSuchUser 
     */
    public abstract void addFleet(Integer raceSeriesId, Fleet fleet)
            throws NoSuchRaceSeries, NoSuchUser, UserNotPermitted;

    /**
     * Returns the collection of fleets in the series with the ID
     * provided
     * @param raceSeriesID  The ID of the race series we are searching in
     * @param pageable      The paging params to use
     * @return The page 
     * @throws UserNotPermitted The logged on user is not permitted to manage this race series
     * @throws NoSuchUser 
     */
    public abstract Page<Fleet> getAllFleetsForSeries(Integer raceSeriesId,
            Pageable p) throws NoSuchUser, UserNotPermitted;

    /**
     * Returns a list containing all the fleets for the series selected.
     * @param raceSeriesId The ID of the race series for which we are retrieving fleets
     * @return The list of fleets in that race series.
     * @throws UserNotPermitted The logged on user is not permitted to manage this race series
     * @throws NoSuchUser 
     */
    public abstract List<Fleet> getAllFleetsForSeries(Integer raceSeriesId)
            throws NoSuchUser, UserNotPermitted;

    /**
     * Updates the DB with the fleet provided
     * @param fleet The updated fleet object
     * @throws UserNotPermitted The logged on user is not permitted to manage this race series
     * @throws NoSuchUser 
     */
    public abstract void updateFleet(Fleet fleet) throws NoSuchUser,
            UserNotPermitted;

    /**
     * Returns the fleet with ID provided
     * @param fleetId The ID of the fleet to return
     * @return The located fleet
     * @throws NoSuchFleet If there is no fleet with that ID
     * @throws UserNotPermitted The logged on user is not permitted to manage this race series
     * @throws NoSuchUser 
     */
    public abstract Fleet getFleetByID(Integer fleetId) throws NoSuchFleet,
            NoSuchUser, UserNotPermitted;

    /**
     * Removes the fleet with ID fleetId from the system
     * @param fleetId The ID of the fleet to remove
     * @throws UserNotPermitted The logged on user is not permitted to manage this race series
     * @throws NoSuchUser 
     */
    public abstract void removeFleet(Integer fleetId) throws NoSuchFleet,
            NoSuchUser, UserNotPermitted;

}