package com.runcible.abbot.service;

import java.util.List;

import com.runcible.abbot.model.Handicap;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

public interface HandicapService
{

    /**
     * Returns a list of handicaps for each boat in the fleet specified. If a boat
     * does not have a handicap a default is chosen (zero or whatever is appropriate
     * for the current handicap system).
     * @param raceSeriesID the ID of the race series this fleet is part of
     * @param the ID of the fleet we are interested in
     * @return The list of handicaps for each boat
     * @throws NoSuchFleet 
     * @throws UserNotPermitted 
     * @throws NoSuchUser 
     */
    public List<Handicap> getHandicapsForFleet(Integer raceSeriesID,Integer fleetId) 
            throws NoSuchUser, UserNotPermitted, NoSuchFleet;

    /**
     * Updates the handicaps for the race with ID specified
     * @param raceID
     * @throws UserNotPermitted 
     * @throws NoSuchUser 
     */
    public void updateHandicapsFromResults(Integer raceID) throws NoSuchUser, UserNotPermitted;

}
