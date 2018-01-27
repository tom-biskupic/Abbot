package com.runcible.abbot.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultStatus;
import com.runcible.abbot.model.ResultType;
import com.runcible.abbot.service.exceptions.DuplicateResult;
import com.runcible.abbot.service.exceptions.NoSuchBoat;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchRaceResult;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

public interface RaceResultService
{

    /**
     * Returns all of the results for a given race
     * 
     * @param raceId    The ID of the race these results are associated with
     * @param p         specifies which page of results to return
     * 
     * @return          The list of results
     * 
     * @throws          UserNotPermitted
     *                  User not permitted to manage this race
     * @throws          NoSuchUser
     *                  Logged on user doesn't exist
     */
    public Page<RaceResult> findAll(Integer raceId, Pageable p)
            throws NoSuchUser, UserNotPermitted;

    /**
     * Returns all of the results for a given race
     * 
     * @param raceId    The ID of the race these results are associated with
     * 
     * @return          The list of results
     * 
     * @throws          UserNotPermitted
     *                  User not permitted to manage this race
     * @throws          NoSuchUser
     *                  Logged on user doesn't exist
     */
    public List<RaceResult> findAll(Integer raceId) throws NoSuchUser, UserNotPermitted;

    /**
     * Finds the result with the ID provided
     * 
     * @param resultId The ID of the result to find
     * 
     * @return
     * 
     * @throws NoSuchUser
     * @throws UserNotPermitted
     * @throws NoSuchRaceResult
     */
    public RaceResult getResultByID(Integer resultId)
            throws NoSuchUser, UserNotPermitted, NoSuchRaceResult;

    /**
     * Adds a result to the datatabase for race with ID raceID
     * 
     * @param raceId The ID of the race to add this result to
     * @param result The result to add.
     * 
     * @throws UserNotPermitted User not authorized to perform this action
     * @throws NoSuchUser       Logged on user does not exist
     * @throws NoSuchBoat       Boat referred to in result does not exist
     * @throws DuplicateResult  We already have a result for this boat
     */
    public void addResult(Integer raceId, RaceResult result)
            throws NoSuchUser, UserNotPermitted, NoSuchBoat, DuplicateResult;

    /**
     * Updates the result with the one provided
     * 
     * @param result    The result to update
     * 
     * @throws NoSuchUser if the logged on user doesn't exist
     * @throws UserNotPermitted If the user is not permitted to manage this race
     * @throws NoSuchRaceResult The race result ID was invalid
     * @throws DuplicateResult We alredy have a result for the boat with this ID
     */
    public void updateResult(RaceResult result)
            throws NoSuchUser, UserNotPermitted, NoSuchRaceResult, DuplicateResult;
    
    /**
     * Removes the specified result from the race.
     * @param resultId The ID of the result to remove
     * @throws NoSuchRaceResult The specified race result was not found
     * @throws UserNotPermitted The logged on user is not permitted to manage this race
     * @throws NoSuchUser The logged on user cannot be found
     */
    public void removeResult(Integer resultId) throws NoSuchRaceResult, NoSuchUser, UserNotPermitted;

    /**
     * Returns a collection of boats in the fleet for the race selected but where there
     * is no result yet for that boat. 
     * @param raceId The ID of the race we are adding a result to
     * @return The list of boats not yet added to this race
     * @throws UserNotPermitted If the logged on user is not permitted to manage this race
     * @throws NoSuchUser If the logged on user is invalid 
     * @throws NoSuchFleet 
     */
    public Collection<Boat> findBoatsNotInRace(Integer raceId) throws NoSuchUser, UserNotPermitted, NoSuchFleet;
    
    /**
     * Add all the other registered boats to the race as non-starters
     * @param raceId The ID of the race to update.
     * @param the result to use (usually DNS or DNC)
     * @throws UserNotPermitted 
     * @throws NoSuchUser 
     * @throws NoSuchFleet 
     * @throws NoSuchBoat 
     */
    public void addNonStartersToRace(
            Integer         raceId, 
            ResultStatus    result) throws NoSuchUser, UserNotPermitted, NoSuchFleet, NoSuchBoat;
    
    
    /**
     * Returns the number of times this boat has won before now
     * @param raceSeriesId
     * @param fleetId
     * @param boatId
     * @param thisRaceDate
     * @return
     */
    public int getWinsForBoatBeforeDate(
            Integer raceSeriesId,
            Integer fleetId,
            Integer boatId,
            Date    thisRaceDate );
}
