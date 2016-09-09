package com.runcible.abbot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.service.exceptions.NoSuchBoat;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

public interface BoatService
{
    /**
     * Returns all the boats in the series identified by raceSeriesId
     * @param raceSeriesId The ID of the race series we want the boats for
     * @param p Pageable parameter to allow paging
     * @return A Page of results.
     * @throws UserNotPermitted The logged on user is not permitted to manage this race series
     * @throws NoSuchUser 
     */
    public abstract Page<Boat> getAllBoatsForSeries(
            Integer raceSeriesId, Pageable p) throws NoSuchUser,
            UserNotPermitted;

    /**
     * Updates the boat in the system
     * @param boat The boat to update
     * @throws UserNotPermitted The logged on user is not permitted to manage this race series
     * @throws NoSuchUser 
     */
    public abstract void updateBoat(Boat boat) throws NoSuchUser, UserNotPermitted;

    /**
     * Adds the boat to the race series with ID specified
     * @param raceSeriesId The ID of the race series to add the competition to
     * @param boat The competition to add
     * @throws NoSuchRaceSeries The race series ID provided does not exists
     * @throws UserNotPermitted The logged on user is not permitted to manage this race series
     * @throws NoSuchUser 
     */
    public abstract void addBoat(
            Integer     raceSeriesId,
            Boat        boat) throws NoSuchRaceSeries, NoSuchUser, UserNotPermitted;

    /**
     * Returns the boat with the ID specified from the system
     * @param boatId ID of the boat to return
     * @return The boat with ID specified
     */
    public abstract Boat getBoatByID(Integer boatId) throws NoSuchBoat, NoSuchUser, UserNotPermitted;

    /**
     * Removes the boat with ID specified
     * @param boatId
     * @throws NoSuchBoat
     * @throws UserNotPermitted The logged on user is not permitted to manage this race series
     * @throws NoSuchUser 
     */
    public abstract void removeBoat(Integer competitionId) throws NoSuchCompetition, NoSuchUser, UserNotPermitted;
}
