package com.runcible.abbot.service;

import java.util.Collection;

import com.runcible.abbot.model.ExportConfig;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

/**
 * The export service provides export of different data from the sailing
 * results database as HTML
 * 
 * @author tom
 *
 */
public interface ExportService
{
    /**
     * Exports the pointstable for the competitions with the ID specified as HTML
     * 
     * @param raceSeriesID      The Race Series the competition is part of
     * @param competitionIds    List of competitions to generate HTML for
     * @return A string containing the points table as HTML
     * @throws NoSuchCompetition    If the competition ID is invalid
     * @throws NoSuchUser           If the logged on user is not valid
     * @throws UserNotPermitted     If the logged on user is not permitted to perform this operation
     * @throws NoSuchFleet          If the fleet associated with the 
     *                              competition is not valid (should not happen)
     */
    public String exportCompetitions(
                Integer             raceSeriesID, 
                Collection<Integer> competitionIds) 
                        throws NoSuchCompetition, NoSuchUser, UserNotPermitted, NoSuchFleet;
    
    /**
     * Exports the races for a fleet as HTML
     * @param raceSeriesID The race series ID
     * @param fleetID The ID of the fleet we are exporting
     * @return The race data as HTML
     * @throws UserNotPermitted     If the logged on user is not permitted to perform this operation
     * @throws NoSuchFleet          If the fleet associated with the 
     */
    public String exportRaces(Integer raceSeriesID, Integer fleetID) throws NoSuchUser, UserNotPermitted, NoSuchFleet;
    
    /**
     * Exports a handicap table for each boat in each race over the series.
     * 
     * @param raceSeriesId
     * @param fleetID
     * @return
     * @throws UserNotPermitted 
     * @throws NoSuchUser 
     * @throws NoSuchFleet 
     */
    /**
     * Exports a combined document containing competition points tables and/or race
     * results for each item in the list. Fleet items are preceded by a fleet-name heading.
     *
     * @param raceSeriesID  The race series
     * @param config        The saved export configuration
     * @return The combined HTML
     */
    public String exportAll(
            Integer      raceSeriesID,
            ExportConfig config)
                    throws NoSuchCompetition, NoSuchUser, UserNotPermitted, NoSuchFleet;

    public String exportHandicapTable(
            Integer raceSeriesId, 
            Integer fleetID,
            boolean shortCourse ) throws NoSuchUser, UserNotPermitted, NoSuchFleet;
}
