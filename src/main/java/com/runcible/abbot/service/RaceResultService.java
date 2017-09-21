package com.runcible.abbot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.service.exceptions.NoSuchBoat;
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
     * @throws UserNotPermitted
     * @throws NoSuchUser
     * @throws NoSuchBoat 
     */
    public void addResult(Integer raceId, RaceResult result)
            throws NoSuchUser, UserNotPermitted, NoSuchBoat;

    /**
     * Updates the result with the one provided
     * 
     * @param result    The result to update
     * 
     * @throws NoSuchUser if the logged on user doesn't exist
     * @throws UserNotPermitted If the user is not permitted to manage this race
     * @throws NoSuchRaceResult
     */
    public void updateResult(RaceResult result)
            throws NoSuchUser, UserNotPermitted, NoSuchRaceResult;
}
