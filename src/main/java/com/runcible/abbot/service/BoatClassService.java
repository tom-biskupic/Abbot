package com.runcible.abbot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.BoatClass;
import com.runcible.abbot.model.BoatDivision;
import com.runcible.abbot.service.exceptions.DuplicateDivision;
import com.runcible.abbot.service.exceptions.NoSuchBoatClass;
import com.runcible.abbot.service.exceptions.NoSuchDivision;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

public interface BoatClassService
{

    /**
     * Returns the collection of boat classes in the series with the ID
     * provided
     * @param raceSeriesID  The ID of the race series we are searching in
     * @param pageable      The paging params to use
     * @return The page 
     * @throws UserNotPermitted Logged on user is not permitted to manage this series
     * @throws NoSuchUser 
     */
    public abstract Page<BoatClass> getAllBoatClassesForSeries(
            Integer raceSeriesID, Pageable pageable) throws NoSuchUser,
            UserNotPermitted;

    /**
     * Returns all the boatclasses configured for the race series specified.
     * @param raceSeriesId The race series we want all the boat classes of
     * @return The list of boat classes.
     * @throws UserNotPermitted Logged on User not permitted to manage this series
     * @throws NoSuchUser 
     */
    public abstract List<BoatClass> getAllBoatClassesForSeries(
            Integer raceSeriesId) throws NoSuchUser, UserNotPermitted;

    /**
     * Adds the boat class to the race series with the ID specified
     * @param raceSeriesId The race series we are adding it to
     * @param boatClass The boat class to add
     * @throws UserNotPermitted Logged on user not permitted to manage this seris
     * @throws NoSuchUser 
     */
    public abstract void addBoatClass(Integer raceSeriesId, BoatClass boatClass)
            throws NoSuchUser, UserNotPermitted;

    /**
     * Updates the specified boat class in the DB
     * @param boatClass The boat class to update.
     * @throws UserNotPermitted Logged on user not permitted to manage this race series 
     * @throws NoSuchUser 
     */
    public abstract void updateBoatClass(BoatClass boatClass)
            throws NoSuchUser, UserNotPermitted;

    /**
     * Removes the boat class with ID specified
     * @param boatClassId
     * @throws NoSuchBoatClass There is no boat class with the ID specified.
     * @throws UserNotPermitted The logged on user is not permitted to manage this race series.
     * @throws NoSuchUser 
     */
    public abstract void removeBoatClass(Integer boatClassId)
            throws NoSuchBoatClass, NoSuchUser, UserNotPermitted;

    /**
     * Returns the boat class with the ID specified
     * @param boatClassId The id of the boat class to retrieve
     * @return The boat class with that ID.
     * @throws NoSuchBoatClass if no boat class with that ID exists 
     * @throws UserNotPermitted Logged on user is not permitted to manage this race series
     * @throws NoSuchUser 
     */
    public abstract BoatClass getBoatClassByID(Integer boatClassId)
            throws NoSuchBoatClass, NoSuchUser, UserNotPermitted;

    /**
     * Updates the boat class with the ID specified and adds the specified
     * division.
     * @throws DuplicateDivision If a division with the same name already exists
     * @throws UserNotPermitted Logged on user not permitted to manage this race series 
     * @throws NoSuchUser 
     * @throws NoSuchBoatClass The boat class we are adding the division to doesn't exist
     */
    public abstract void addDivision(Integer boatClassId, BoatDivision division)
            throws DuplicateDivision, NoSuchUser, UserNotPermitted, NoSuchBoatClass;

    /**
     * Updates the boat class with the ID specified and updates the provided
     * division.
     * @throws DuplicateDivision If the name has been changed to that of another division 
     * @throws NoSuchDivision If the division being updated doesn't exist or is not attached to this boat class.
     * @throws NoSuchBoatClass The boat class ID specified does not exists
     * @throws UserNotPermitted  The logged on user is not permitted to manage this race series
     * @throws NoSuchUser 
     */
    public abstract void updateDivision(Integer boatClassId,
            BoatDivision division) throws DuplicateDivision, NoSuchDivision,
            NoSuchBoatClass, NoSuchUser, UserNotPermitted;

    /**
     * Removes the specified division from the boatclass with ID provided.
     * @param boatClassId The ID of the boat class we are updating
     * @param divisionId The ID of the division to be removed
     * @throws NoSuchDivision If the division isn't part of that boat class.
     * @throws NoSuchBoatClass The boat class with the ID provided does not exist
     * @throws UserNotPermitted Logged on user is not permitted to manage this race series
     * @throws NoSuchUser 
     */
    public abstract void removeDivision(Integer boatClassId, Integer divisionId)
            throws NoSuchDivision, NoSuchBoatClass, NoSuchUser,
            UserNotPermitted;

}